package elektrogo.front.ui.valorarUsuari

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.ViewManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import elektrogo.front.R
import elektrogo.front.controller.FrontendController
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.Rating
import kotlinx.coroutines.runBlocking


class ValorarUsuariDialog : DialogFragment() {

    private var stars = arrayListOf<ImageView>()
    private var puntuacio = 0
    private lateinit var ratedUser:String
    private lateinit var commentView: TextInputEditText
    private lateinit var valorarBtn: Button
    private lateinit var eliminarBtn: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            builder.apply {

                setTitle("Valora")

                ratedUser = arguments?.getString("guestUser")!!
                setMessage("Com vols valorar a ${ratedUser}?")

                val guiValorar = layoutInflater.inflate(R.layout.valorar_usuari_fragment, null)
                setView(guiValorar)

                stars.add(guiValorar.findViewById(R.id.star1) as ImageView)
                stars.add(guiValorar.findViewById(R.id.star2) as ImageView)
                stars.add(guiValorar.findViewById(R.id.star3) as ImageView)
                stars.add(guiValorar.findViewById(R.id.star4) as ImageView)
                stars.add(guiValorar.findViewById(R.id.star5) as ImageView)

                commentView = guiValorar.findViewById(R.id.comment)

                valorarBtn = guiValorar.findViewById(R.id.buttonValorarUsuari)
                eliminarBtn = guiValorar.findViewById(R.id.buttonEliminarValoracioUsuari)

                valorarBtn.setOnClickListener {

                    val userWhoRates = SessionController.getUsername(requireActivity()) //TODO: SI SE COGE PARA MIRAR SI SE MUESTRA EL BOTON ELIMINAR, HACER SOLO UNA LLAMADA Y GUARDAR EN UNA VARIABLE
                    val valoracio = Rating(userWhoRates, ratedUser, puntuacio*2, commentView.text.toString())

                    var status = -1
                    try { status = runBlocking{ FrontendController.rateUser(valoracio) } }
                    catch (e: Exception) {}

                    if (status == 200) Toast.makeText(activity, "Has valorat a $ratedUser amb $puntuacio estrelles.", Toast.LENGTH_SHORT).show()
                    else Toast.makeText(activity, "No s'ha pogut valorar a $ratedUser.", Toast.LENGTH_SHORT).show()

                    dismiss() // tanca el dialog
                }

                eliminarBtn.setOnClickListener {

                    val userWhoRates = SessionController.getUsername(requireActivity()) //TODO: SI SE COGE PARA MIRAR SI SE MUESTRA EL BOTON ELIMINAR, HACER SOLO UNA LLAMADA Y GUARDAR EN UNA VARIABLE

                    var status = -1
                    try { status = runBlocking{ FrontendController.unrateUser(userWhoRates, ratedUser) } }
                    catch (e: Exception) {}

                    if (status == 200) Toast.makeText(activity, "Has eliminat la valoració.", Toast.LENGTH_SHORT).show()
                    else Toast.makeText(activity, "No s'ha pogut eliminar la valoració.", Toast.LENGTH_SHORT).show()

                    dismiss() // tanca el dialog
                }

                //TODO: Aquí mirar si el usuario ya ha sido valorado, para mostrar el botón de eliminar, y además setear las estrellas a la valoracion que toque

                var rated = true
                if (!rated) {
                    (eliminarBtn.parent as ViewManager).removeView(eliminarBtn)
                }

                //logica per canviar la puntuacio tocant les estrelles
                for (i in stars.indices) {
                    stars[i].setOnClickListener {
                        puntuacio = if (puntuacio != i+1) i+1
                        else 0
                        setStars()
                    }
                }


            }

            // Create el dialog
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    private fun setStars() {
        for (i in stars.indices) {
            if (i < puntuacio) stars[i].setImageResource(R.drawable.ic_starplena)
            else stars[i].setImageResource(R.drawable.ic_starbuida)
        }
    }


}