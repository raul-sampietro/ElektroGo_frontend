package elektrogo.front.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import elektrogo.front.R
import elektrogo.front.ui.vehicleAdd.AddVehicle
import elektrogo.front.databinding.ProfileFragmentBinding
import elektrogo.front.ui.vehicleList.VehicleListActivity
import elektrogo.front.ui.vehicleList.VehicleListFragment
import elektrogo.front.ui.vehicleList.VehicleListViewModel

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.profile_fragment, container, false)

        val buttonCars: Button = view.findViewById(R.id.AddVehicleButton)
        buttonCars.setOnClickListener {
            val intent = Intent(container?.context, VehicleListActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}