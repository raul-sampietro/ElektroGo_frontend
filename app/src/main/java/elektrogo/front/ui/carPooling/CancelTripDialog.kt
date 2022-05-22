package elektrogo.front.ui.carPooling

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import elektrogo.front.R

class CancelTripDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = androidx.appcompat.app.AlertDialog.Builder(it)

            builder.apply {

                val textMotiu = layoutInflater.inflate(R.layout.fragment_cancelar_trajecte, null)
                setView(textMotiu)

                val motiu = textMotiu.findViewById<TextInputEditText>(R.id.motiu)

                setTitle("Confirmació")

                setMessage("Dona un motiu i confirma")

                setPositiveButton("CONFIRMAR",
                    DialogInterface.OnClickListener { dialog, id ->

                        //TODO: crida http a back amb l'url que digui en Gerard i control d'errors, i treue strings hardcodejades
                        Toast.makeText(context, "Motiu: ${motiu.text.toString()}", Toast.LENGTH_LONG).show()

                        requireActivity().finish()
                        Toast.makeText(context, "Trajecte cancel·lat", Toast.LENGTH_LONG).show()
                    })
            }
            // Crea el dialeg
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }













}