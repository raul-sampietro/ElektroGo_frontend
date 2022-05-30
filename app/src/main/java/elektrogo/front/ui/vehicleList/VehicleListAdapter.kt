package elektrogo.front.ui.vehicleList

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.squareup.picasso.Picasso
import elektrogo.front.controller.FrontendController
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.Vehicle
import kotlinx.coroutines.runBlocking

class VehicleListAdapter(private val context : Activity, private val vehicleList : ArrayList<Vehicle>)
    : ArrayAdapter<Vehicle>(context, R.layout.fragment_vehicle_list_item, vehicleList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.fragment_vehicle_list_item, null)

        val numberPlate : TextView = view.findViewById(R.id.listNumberPlate)
        val verificated : ImageView = view.findViewById(R.id.vehicleVerificat)
        val brand : TextView = view.findViewById(R.id.listBrand)
        val model : TextView = view.findViewById(R.id.listModel)
        val fabricationYear : TextView = view.findViewById(R.id.listFabricationYear)
        val seats : TextView = view.findViewById(R.id.listSeats)

        val v = vehicleList[position]
        val imageViewPhoto : ImageView =view.findViewById(R.id.vehicleImage)
        val nPlate = v.numberPlate
        Picasso.get().load("http://10.4.41.58:8080/vehicles/${nPlate}/image").into(imageViewPhoto)
        numberPlate.text = nPlate
        if (v.verification == "verified")
            verificated.setImageResource(R.drawable.verificat)
        brand.text = v.brand
        model.text = v.model
        fabricationYear.text = v.fabricationYear.toString()
        seats.text = v.seats.toString()

        val deleteVehicleButton: Button = view.findViewById(R.id.deleteVehicleButton)

        deleteVehicleButton.setOnClickListener {
            val alertDialog: AlertDialog? = parent.context.let {
                val builder = AlertDialog.Builder(it)
                builder.setMessage(context.getString(R.string.preguntaEsborrarVehicle) + " ${v.numberPlate}?")
                builder.apply {
                    setPositiveButton(R.string.si,
                        DialogInterface.OnClickListener { dialog, id ->
                            Toast.makeText(parent.context, context.getString(R.string.vehicleErased), Toast.LENGTH_LONG).show()
                            deleteVehicle(v.numberPlate, SessionController.getUsername(parent.context))
                            val intent = Intent(parent.context, VehicleListActivity::class.java)
                            parent.context.startActivity(intent)
                        })
                    setNegativeButton(R.string.no,
                        DialogInterface.OnClickListener { dialog, id ->
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