package elektrogo.front.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.facebook.FacebookSdk.getApplicationContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import elektrogo.front.MainActivity
import elektrogo.front.R
import elektrogo.front.controller.FrontendController
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

        val displayName: TextView = view.findViewById(R.id.displayName)
        val acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext())
        if (acct != null) {
            displayName.text = acct.displayName
        }
        else displayName.text = "nullaccount"

        val setButton: Button = view.findViewById(R.id.setUsernameButton)
        val usernameField: com.google.android.material.textfield.TextInputLayout = view.findViewById(R.id.textFieldUsername)

        setButton.setOnClickListener {
            val new = User(
                usernameField.editText!!.text.toString(),
                acct!!.email!!,
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

        return view
    }

    private fun addUser(user: User): Int = runBlocking {
        FrontendController.addUser(user)
    }

}