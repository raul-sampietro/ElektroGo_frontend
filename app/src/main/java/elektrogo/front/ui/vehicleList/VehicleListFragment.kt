package elektrogo.front.ui.vehicleList

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import elektrogo.front.*

class VehicleListFragment : Fragment() {

    companion object {
        fun newInstance() = VehicleListFragment()
    }

    private lateinit var viewModel: VehicleListViewModel
    private lateinit var vehicleList: ArrayList<Vehicle>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_vehicle_list, container, false)
        val listView: ListView = view.findViewById(R.id.list_view)
        val newVehicleButton: com.google.android.material.floatingactionbutton.FloatingActionButton = view.findViewById(R.id.fab)

        vehicleList = viewModel.getVehicleList("Test")

        newVehicleButton.setOnClickListener {
            //Toast.makeText(container?.context, "Clicked", Toast.LENGTH_LONG).show()
            val intent = Intent(container?.context, AddVehicle::class.java)
            container?.context?.startActivity(intent)
        }

        /*
        // Testing data
        val vehicle1 = Vehicle("Opel", "Corsa", "0049HNZ", 234, 2007, 5, null)
        val vehicle2 = Vehicle("Nissan", "X-Trail", "0050AAA", 300, 2001, 7, null)

        val list = ArrayList<Vehicle>()
        list.add(vehicle1)
        list.add(vehicle2)
        list.add(vehicle1)
        list.add(vehicle2)
        list.add(vehicle1)
        list.add(vehicle2)
        list.add(vehicle1)
        list.add(vehicle2)
        list.add(vehicle1)
        list.add(vehicle2)
        list.add(vehicle1)
        list.add(vehicle2)
        list.add(vehicle1)
        list.add(vehicle2)
        list.add(vehicle1)
        list.add(vehicle2)
        listView.adapter = VehicleListAdapter(container?.context as Activity,list)
        */


        listView.adapter = VehicleListAdapter(container?.context as Activity, vehicleList)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[VehicleListViewModel::class.java]
    }
}