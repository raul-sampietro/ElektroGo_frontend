package elektrogo.front.ui.CarPooling

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.textfield.TextInputEditText
import elektrogo.front.R
import elektrogo.front.ui.valorarUsuari.ValorarUsuariFragment

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

                        Toast.makeText(context, "Motiu: ${motiu.text.toString()}", Toast.LENGTH_LONG).show()

                        requireActivity().finish()
                        Toast.makeText(context, "Trajecte cancel·lat", Toast.LENGTH_LONG).show()
                    })
                setNegativeButton("ENRERE",
                    DialogInterface.OnClickListener { dialog, id -> })
            }
            // Crea el dialeg
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }













}