package elektrogo.front.ui.vehicleList

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import elektrogo.front.controller.FrontendController
import elektrogo.front.R
import elektrogo.front.model.Vehicle
import kotlinx.coroutines.runBlocking

class VehicleListAdapter(private val context : Activity, private val vehicleList : ArrayList<Vehicle>)
    : ArrayAdapter<Vehicle>(context, R.layout.fragment_vehicle_list_item, vehicleList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.fragment_vehicle_list_item, null)

        // TODO imageView
        val numberPlate : TextView = view.findViewById(R.id.listNumberPlate)
        val brand : TextView = view.findViewById(R.id.listBrand)
        val model : TextView = view.findViewById(R.id.listModel)
        val fabricationYear : TextView = view.findViewById(R.id.listFabricationYear)
        val seats : TextView = view.findViewById(R.id.listSeats)

        val v = vehicleList[position]
        numberPlate.text = v.numberPlate
        brand.text = v.brand
        model.text = v.model
        fabricationYear.text = v.fabricationYear.toString()
        seats.text = v.seats.toString()

        val deleteVehicleButton: Button = view.findViewById(R.id.deleteVehicleButton)

        deleteVehicleButton.setOnClickListener {
            //Toast.makeText(parent.context, "Clicked", Toast.LENGTH_LONG).show()
            val alertDialog: AlertDialog? = parent.context.let {
                val builder = AlertDialog.Builder(it)
                // TODO 3 hardcoded strings
                builder.setMessage("Segur?")
                builder.apply {
                    setPositiveButton("OK",
                        DialogInterface.OnClickListener { dialog, id ->
                            Toast.makeText(parent.context, "Yes", Toast.LENGTH_LONG).show()
                            deleteVehicle(v.numberPlate, "Test")
                        })
                    setNegativeButton("NO",
                        DialogInterface.OnClickListener { dialog, id ->
                            Toast.makeText(parent.context, "No", Toast.LENGTH_LONG).show()
                        })
                }
                // Create the AlertDialog
                builder.create()
            }
            alertDialog?.show()
        }

        return view
    }

    private fun deleteVehicle(numberPlate: String, username: String) = runBlocking() {
        FrontendController.deleteVehicle(numberPlate, username)
    }
}