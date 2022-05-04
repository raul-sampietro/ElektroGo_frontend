package elektrogo.front.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import elektrogo.front.MainActivity
import elektrogo.front.R
import elektrogo.front.controller.FrontendController
import elektrogo.front.controller.FrontendController.addDriver
import elektrogo.front.controller.session.Session
import elektrogo.front.controller.session.SessionController
import elektrogo.front.databinding.ProfileFragmentBinding
import elektrogo.front.model.Driver
import elektrogo.front.ui.carPooling.TripsActivity
import elektrogo.front.ui.login.LoginActivity
import elektrogo.front.ui.vehicleList.VehicleListActivity
import kotlinx.coroutines.runBlocking


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

        val username: TextView = view.findViewById(R.id.profile_username)
        username.text = SessionController.getUsername(requireActivity())

        val buttonDriver: Button = view.findViewById(R.id.profile_become_driver)
        buttonDriver.setOnClickListener {
            val new = Driver(SessionController.getUsername(requireContext()))
            val httpStatus: Int = addDriver(new)
            Toast.makeText(requireContext(), httpStatus.toString(), Toast.LENGTH_SHORT).show()
        }

        val buttonCars: Button = view.findViewById(R.id.AddVehicleButton)
        buttonCars.setOnClickListener {
            val intent = Intent(container?.context, VehicleListActivity::class.java)
            startActivity(intent)
        }

        val buttonTrips: Button = view.findViewById(R.id.profile_MyTrips)
        buttonTrips.setOnClickListener {
            val intent = Intent(container?.context, TripsActivity::class.java)
            startActivity(intent)
        }

        // Logout button
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        view.findViewById<Button>(R.id.profile_logout).setOnClickListener {
            mGoogleSignInClient.signOut()
                .addOnCompleteListener(requireActivity(), OnCompleteListener  {
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(intent)
                    SessionController.setCurrentSession(null)
                    Toast.makeText(requireActivity(), "Successfully signed out", Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                })

        }

        val deleteButton: Button = view.findViewById(R.id.profile_delete_account)
        deleteButton.setOnClickListener {

        }

        return view
    }

    private fun addDriver(driver: Driver): Int = runBlocking {
        FrontendController.addDriver(driver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
