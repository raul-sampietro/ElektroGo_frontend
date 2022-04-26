package elektrogo.front.ui.login

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import elektrogo.front.R


class PostLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_login)

        var text: TextView = findViewById(R.id.textBox)
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            text.text = "Hello, ${acct.displayName}!\n${acct.givenName}\n${acct.familyName}\n${acct.email}\n${acct.id}\n${acct.photoUrl}"
        }
        else text.text = "nullaccount"
    }
}