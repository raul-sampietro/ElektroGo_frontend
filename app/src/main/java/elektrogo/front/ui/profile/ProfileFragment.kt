package elektrogo.front.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.squareup.picasso.Picasso
import elektrogo.front.R
import elektrogo.front.controller.FrontendController
import elektrogo.front.controller.session.Session
import elektrogo.front.controller.session.SessionController
import elektrogo.front.databinding.ProfileFragmentBinding
import elektrogo.front.model.Driver
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

        val user = SessionController.getUsername(requireActivity());
        val imageViewProfile : ImageView = view.findViewById(R.id.profile_image)
        val imagePath = viewModel.getUsersProfilePhoto(user)
        if (!imagePath.equals("null")  or !imagePath.equals("") ) Picasso.get().load(imagePath).into(imageViewProfile)
        else imageViewProfile.setImageResource(R.drawable.avatar)

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

    private fun renderRating(ratingPassed: Double, view: View?) {
        val star1: ImageView = requireView().findViewById(R.id.estrella1)
        val star2: ImageView = requireView().findViewById(R.id.estrella2)
        val star3: ImageView = requireView().findViewById(R.id.estrella3)
        val star4: ImageView = requireView().findViewById(R.id.estrella4)
        val star5: ImageView = requireView().findViewById(R.id.estrella5)

        var rating = ratingPassed / 2
        var decimalValue = rating - rating.toInt()
        var enterValue = rating.toInt()

        when (enterValue) {
            0 -> {
                if (decimalValue >= 0.25 && decimalValue < 0.75) {
                    star1.setImageResource(R.drawable.ic_starmigplena)
                } else if (decimalValue >= 0.75) {
                    star1.setImageResource(R.drawable.ic_starplena)
                } else {
                    star1.setImageResource(R.drawable.ic_starbuida)
                }
                star2.setImageResource(R.drawable.ic_starbuida)
                star3.setImageResource(R.drawable.ic_starbuida)
                star4.setImageResource(R.drawable.ic_starbuida)
                star5.setImageResource(R.drawable.ic_starbuida)
            }
            1 -> {
                star1.setImageResource(R.drawable.ic_starplena)
                if (decimalValue >= 0.25 && decimalValue < 0.75) {
                    star2.setImageResource(R.drawable.ic_starmigplena)
                } else if (decimalValue >= 0.75) {
                    star2.setImageResource(R.drawable.ic_starplena)
                } else {
                    star2.setImageResource(R.drawable.ic_starbuida)
                }
                star3.setImageResource(R.drawable.ic_starbuida)
                star4.setImageResource(R.drawable.ic_starbuida)
                star5.setImageResource(R.drawable.ic_starbuida)
            }
            2 -> {
                star1.setImageResource(R.drawable.ic_starplena)
                star2.setImageResource(R.drawable.ic_starplena)
                if (decimalValue >= 0.25 && decimalValue < 0.75) {
                    star3.setImageResource(R.drawable.ic_starmigplena)
                } else if (decimalValue >= 0.75) {
                    star3.setImageResource(R.drawable.ic_starplena)
                } else {
                    star3.setImageResource(R.drawable.ic_starbuida)
                }
                star4.setImageResource(R.drawable.ic_starbuida)
                star5.setImageResource(R.drawable.ic_starbuida)
            }
            3 -> {
                star1.setImageResource(R.drawable.ic_starplena)
                star2.setImageResource(R.drawable.ic_starplena)
                star3.setImageResource(R.drawable.ic_starplena)
                if (decimalValue >= 0.25 && decimalValue < 0.75) {
                    star4.setImageResource(R.drawable.ic_starmigplena)
                } else if (decimalValue >= 0.75) {
                    star4.setImageResource(R.drawable.ic_starplena)
                } else {
                    star4.setImageResource(R.drawable.ic_starbuida)
                }
                star5.setImageResource(R.drawable.ic_starbuida)
            }
            4 -> {
                star1.setImageResource(R.drawable.ic_starplena)
                star2.setImageResource(R.drawable.ic_starplena)
                star3.setImageResource(R.drawable.ic_starplena)
                star4.setImageResource(R.drawable.ic_starplena)

                if (decimalValue >= 0.25 && decimalValue < 0.75) {
                    star5.setImageResource(R.drawable.ic_starmigplena)
                } else if (decimalValue >= 0.75) {
                    star5.setImageResource(R.drawable.ic_starplena)
                } else {
                    star5.setImageResource(R.drawable.ic_starbuida)
                }
            }
            5 -> {
                star1.setImageResource(R.drawable.ic_starplena)
                star2.setImageResource(R.drawable.ic_starplena)
                star3.setImageResource(R.drawable.ic_starplena)
                star4.setImageResource(R.drawable.ic_starplena)
                star5.setImageResource(R.drawable.ic_starplena)
            }
        }
    }

    private fun addDriver(driver: Driver): Int = runBlocking {
        FrontendController.addDriver(driver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
