package elektrogo.front.ui.CarPooling

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import elektrogo.front.R

class CancelTripDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = androidx.appcompat.app.AlertDialog.Builder(it)
            builder.apply {

                val inflater = layoutInflater
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                setView(inflater.inflate(R.layout.valorar_usuari_fragment, null)) //per provar, carregare el de valoracio

                setTitle("Confirmació")

                setMessage("Segur que vols cancel·lar aquest trajecte?")

                setPositiveButton("SÍ",
                    DialogInterface.OnClickListener { dialog, id ->



                        requireActivity().finish()
                        Toast.makeText(context, "Trajecte cancel·lat", Toast.LENGTH_LONG).show()
                    })
                setNegativeButton("NO",
                    DialogInterface.OnClickListener { dialog, id -> })
            }
            // Crea el dialeg
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

}