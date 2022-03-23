package elektrogo.front

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class VehicleListAdapter(private val context : Activity, private val vehicleList : ArrayList<Vehicle>)
    : ArrayAdapter<Vehicle>(context, R.layout.list_item, vehicleList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.list_item, null)

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

        return view
    }
}