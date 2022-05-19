package elektrogo.front.ui.valorarUsuari

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import elektrogo.front.R


class ValorarUsuariDialog : DialogFragment() {

    private var stars = arrayListOf<ImageView>()
    private var puntuacio = 0


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            builder.apply {

                setTitle("Valora")

                setMessage("Com vols valorar aquest usuari?")

                val guiValorar = layoutInflater.inflate(R.layout.valorar_usuari_fragment, null)
                setView(guiValorar)

                stars.add(guiValorar.findViewById(R.id.star1) as ImageView)
                stars.add(guiValorar.findViewById(R.id.star2) as ImageView)
                stars.add(guiValorar.findViewById(R.id.star3) as ImageView)
                stars.add(guiValorar.findViewById(R.id.star4) as ImageView)
                stars.add(guiValorar.findViewById(R.id.star5) as ImageView)



                for (i in stars.indices) {
                    stars[i].setOnClickListener {
                        puntuacio = if (puntuacio != i+1) i+1
                        else 0
                        setStars()
                    }
                }




                setPositiveButton("START",
                    DialogInterface.OnClickListener { dialog, id ->
                        // START THE GAME!
                    }
                )

                setNegativeButton("CANCEL",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    }
                )

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