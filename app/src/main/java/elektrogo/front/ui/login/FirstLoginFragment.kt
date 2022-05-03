package elektrogo.front.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.facebook.AccessToken
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.GraphRequest
import com.facebook.Profile
import com.google.android.gms.auth.api.signin.GoogleSignIn
import elektrogo.front.MainActivity
import elektrogo.front.R
import elektrogo.front.controller.FrontendController
import elektrogo.front.controller.session.FacebookSessionAdapter
import elektrogo.front.controller.session.GoogleSessionAdapter
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.User
import kotlinx.coroutines.runBlocking

class FirstLoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first_login, container, false)

        val acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext())
        val accessToken = AccessToken.getCurrentAccessToken()

        val displayName: TextView = view.findViewById(R.id.displayName)
        val setButton: Button = view.findViewById(R.id.setUsernameButton)
        val usernameField: com.google.android.material.textfield.TextInputLayout = view.findViewById(R.id.textFieldUsername)

        //login google
        if (acct != null) {
            displayName.text = acct.displayName
            setButton.setOnClickListener {
                val new = User(
                    usernameField.editText!!.text.toString(),
                    acct.email!!,
                    acct.id!!,
                    "GOOGLE",
                    acct.displayName!!,
                    acct.givenName!!,
                    acct.familyName!!,
                    acct.photoUrl.toString()
                )
                addUser(new)
                SessionController.setCurrentSession(GoogleSessionAdapter())
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
            }
        }
        //login facebook
        else if (accessToken != null && !accessToken.isExpired) {
            val profile = Profile.getCurrentProfile()
            displayName.text = profile!!.name
            setButton.setOnClickListener {
                lateinit var new: User
                val request = GraphRequest.newMeRequest(
                    accessToken
                ) { obj, response ->
                    new = User(
                        usernameField.editText!!.text.toString(),
                        obj!!.optString("email"),
                        obj.optString("id"),
                        "FACEBOOK",
                        obj.optString("name"),
                        obj.optString("first_name"),
                        obj.optString("last_name"),
                        "https://graph.facebook.com/${obj.optString("id")}/picture?type=large"
                    )
                }
                request.executeAsync()
                addUser(new)
                SessionController.setCurrentSession(FacebookSessionAdapter())
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
            }
        }
        //no login
        else {
            displayName.text = "nullaccount"
        }

        return view
    }

    private fun addUser(user: User): Int = runBlocking {
        FrontendController.addUser(user)
    }

}