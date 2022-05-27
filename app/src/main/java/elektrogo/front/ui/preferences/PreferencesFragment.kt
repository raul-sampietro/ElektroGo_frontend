package elektrogo.front.ui.preferences

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import elektrogo.front.MainActivity
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.languages.Preference
import elektrogo.front.ui.login.LoginActivity


class PreferencesFragment : Fragment() {
    private lateinit var radiobuttonGroup: RadioGroup
    private var modelView = PreferencesViewModel()
    lateinit var preference: Preference
    val mainActivity = MainActivity()
    private var languageSelected : String = "ca"
    lateinit var toolbar2:android.widget.Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preferences, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            toolbar2  = requireActivity().findViewById(R.id.toolbar_main)
            toolbar2.title = getString(R.string.ajustos)
            requireActivity().setActionBar(toolbar2)

            val username : TextView = requireActivity().findViewById(R.id.usernameText)
            val email : TextView = requireActivity().findViewById(R.id.userMail)
            val deleteButton : Button = requireActivity().findViewById(R.id.deleteButton)

            username.text= SessionController.getUsername(requireContext())
            email.text= SessionController.getEmail(requireContext())

            radiobuttonGroup = requireActivity().findViewById(R.id.radiobuttongroup)

            preference = Preference(requireContext())

            //Agafem la configuracio al inici
            if (preference.getLoginCount().equals("ca")){
                radiobuttonGroup.check(R.id.catalanButton)
            }
            else if (preference.getLoginCount().equals("en")){
                radiobuttonGroup.check(R.id.englishButton)
            } else {
                radiobuttonGroup.check(R.id.spanishButton)
            }

            radiobuttonGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radiobuttonGroup, checkedId ->
                val radioButton: View = radiobuttonGroup.findViewById(checkedId)
                when (radiobuttonGroup.indexOfChild(radioButton)) {
                    0 -> {
                        preference.setLoginCount("en")
                        languageSelected = "en"
                        //   mainActivity.changeSelectedLanguage(languageSelected)
                        requireActivity().finish();
                        requireActivity().overridePendingTransition(0, 0);
                        requireActivity().startActivity(requireActivity().getIntent());
                        requireActivity().overridePendingTransition(0, 0);
                    }
                    1 -> {
                        preference.setLoginCount("ca")
                        languageSelected = "ca"
                        //    mainActivity.changeSelectedLanguage(languageSelected)
                        requireActivity().finish();
                        requireActivity().overridePendingTransition(0, 0);
                        requireActivity().startActivity(requireActivity().getIntent());
                        requireActivity().overridePendingTransition(0, 0);
                    }
                    2 -> {
                        preference.setLoginCount("es")
                        languageSelected = "es"
                        //    mainActivity.changeSelectedLanguage(languageSelected)
                        requireActivity().finish();
                        requireActivity().overridePendingTransition(0, 0);
                        requireActivity().startActivity(requireActivity().getIntent());
                        requireActivity().overridePendingTransition(0, 0);
                    }
                }
            })

            deleteButton.setOnClickListener {
                val status = modelView.deleteAccount(SessionController.getUsername(requireContext()))
                if (status != 200)  Toast.makeText(requireContext(),"Hi ha hagut un problema. No s'ha pogut esborrar el compte.", Toast.LENGTH_SHORT).show()
                else {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build()
                    val mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
                    mGoogleSignInClient.signOut()
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    SessionController.setCurrentSession(null)
                    Toast.makeText(requireContext(), "Successfully deleted the account", Toast.LENGTH_SHORT).show()
                    requireActivity().finish();
                }
            }
        }

    }