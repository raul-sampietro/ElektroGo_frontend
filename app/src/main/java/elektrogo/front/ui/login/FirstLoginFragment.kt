package elektrogo.front.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        var text: TextView = view.findViewById(R.id.textBox)
        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (acct != null) {
            text.text = "Hello, ${acct.displayName}!\n${acct.givenName}\n${acct.familyName}\n${acct.email}\n${acct.id}\n${acct.photoUrl}"
        }
        else text.text = "nullaccount"

        return view
    }

}