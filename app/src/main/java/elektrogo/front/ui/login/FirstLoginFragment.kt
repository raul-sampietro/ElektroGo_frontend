package elektrogo.front.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import elektrogo.front.R

class FirstLoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first_login, container, false)

        val displayName: TextView = view.findViewById(R.id.displayName)
        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (acct != null) {
            displayName.text = acct.displayName
        }
        else displayName.text = "nullaccount"

        val setButton: Button = view.findViewById(R.id.setUsernameButton)
        setButton.setOnClickListener {
            val username: com.google.android.material.textfield.TextInputEditText = it.findViewById(R.id.textFieldUsername)
            val text: TextView = it.findViewById(R.id.usernameText)
            text.text = username.text
        }

        return view
    }

}