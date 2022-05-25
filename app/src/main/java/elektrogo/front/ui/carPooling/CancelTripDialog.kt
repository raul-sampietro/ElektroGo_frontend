package elektrogo.front.ui.carPooling

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import elektrogo.front.R
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.CanceledTrip
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class CancelTripDialog : DialogFragment() {

    @SuppressLint("SimpleDateFormat")
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

                        val tripID = arguments?.getString("tripID")!!

                        val currentDate = Calendar.getInstance().time
                        val formatter = SimpleDateFormat("yyyy-MM-dd")
                        val dayCanceled = formatter.format(currentDate)

                        val trajecteCancelat = CanceledTrip(tripID.toLong(), dayCanceled, motiu.text.toString())

                        var status = -1
                        try { status = runBlocking{ FrontendController.cancelTrip(trajecteCancelat) } }
                        catch (e: Exception) {}

                        if (status == 200) Toast.makeText(context, "Trajecte cancel·lat", Toast.LENGTH_SHORT).show()
                        else Toast.makeText(activity, "No s'ha pogut canel·lar el trajecte.", Toast.LENGTH_SHORT).show()

                        requireActivity().finish()
                    })
            }
            // Crea el dialeg
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }













}