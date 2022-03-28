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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import elektrogo.front.*
import elektrogo.front.model.Vehicle
import elektrogo.front.ui.vehicleAdd.AddVehicle

class VehicleListFragment : Fragment() {

    companion object {
        fun newInstance() = VehicleListFragment()
    }

    private lateinit var viewModel: VehicleListViewModel
    private lateinit var vehicleList: ArrayList<Vehicle>
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

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
            resultLauncher.launch(intent)
        }

        listView.adapter = VehicleListAdapter(container?.context as Activity, vehicleList)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[VehicleListViewModel::class.java]
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //TODO result OK
            }
        }
    }
}