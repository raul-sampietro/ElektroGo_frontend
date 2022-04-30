package elektrogo.front.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import elektrogo.front.databinding.ProfileFragmentBinding
import elektrogo.front.ui.valorarUsuari.ValorarUsuariActivity
import elektrogo.front.ui.vehicleList.VehicleListActivity

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val AddVehicleButton = binding.AddVehicleButton
        AddVehicleButton.setOnClickListener{
            val addVehicleIntent = Intent(activity, VehicleListActivity::class.java)
            startActivity(addVehicleIntent)
        }

        val valorarUsuariButton = binding.ValorarUsuariButton
        valorarUsuariButton.setOnClickListener{
            val valorarUsuariIntent = Intent(activity, ValorarUsuariActivity::class.java)
            startActivity(valorarUsuariIntent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}