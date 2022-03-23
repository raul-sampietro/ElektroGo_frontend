package elektrogo.front.ui.vehicleList

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elektrogo.front.R

class VehicleListItemFragment : Fragment() {

    companion object {
        fun newInstance() = VehicleListItemFragment()
    }

    private lateinit var viewModel: VehicleListItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vehicle_list_item, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[VehicleListItemViewModel::class.java]
    }

}