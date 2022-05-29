package elektrogo.front.ui.valorarUsuari

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
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

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            builder.apply {

                val userWhoRates = SessionController.getUsername(requireActivity())
                ratedUser = arguments?.getString("guestUser")!!
                var rated = false

                setTitle(getString(R.string.Valora))
                setMessage(getString(R.string.ComValorar) + " $ratedUser?")

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

                    val valoracio = Rating(userWhoRates, ratedUser, puntuacio*2, commentView.text.toString())
                    var status = -1
                    try { status = runBlocking{ FrontendController.rateUser(valoracio) } }
                    catch (e: Exception) {}

                    if (status == 201) Toast.makeText(activity, getString(R.string.HasValoratA) + " $ratedUser " + getString(R.string.amb) + " $puntuacio " + getString(R.string.Estrelles), Toast.LENGTH_SHORT).show()
                    else Toast.makeText(activity, getString(R.string.NoSHaPogutValorarA) + " $ratedUser.", Toast.LENGTH_SHORT).show()

                    dismiss() // tanca el dialog
                }

                eliminarBtn.setOnClickListener {

                    var status = -1
                    try { status = runBlocking{ FrontendController.unrateUser(userWhoRates, ratedUser) } }
                    catch (e: Exception) {}

                    if (status == 200) Toast.makeText(activity, getString(R.string.ValoracioEliminada), Toast.LENGTH_SHORT).show()
                    else Toast.makeText(activity, getString(R.string.ErrorEliminarValoracio), Toast.LENGTH_SHORT).show()

                    dismiss() // tanca el dialog
                }

                var statAndRating: Pair<Int, Rating?>
                try { statAndRating = runBlocking{ FrontendController.getRating(userWhoRates, ratedUser) } }
                catch (e: Exception) {
                    statAndRating = Pair(-1, null)
                }

                if (statAndRating.first == 200 && (statAndRating.second != null)) {
                    rated = true
                    puntuacio = statAndRating.second!!.points/2
                    if (statAndRating.second!!.comment != null) {
                        commentView.text = statAndRating.second!!.comment!!.toEditable()
                    }
                }
                if (!rated) (eliminarBtn.parent as ViewManager).removeView(eliminarBtn)
                else {
                    setStars()
                    valorarBtn.text = getString(R.string.ModificarValoracio)
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

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)


}