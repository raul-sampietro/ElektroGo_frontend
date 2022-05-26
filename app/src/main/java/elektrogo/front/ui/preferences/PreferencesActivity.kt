package elektrogo.front.ui.preferences

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import elektrogo.front.R
import elektrogo.front.controller.session.Session
import elektrogo.front.controller.session.SessionController
import elektrogo.front.languages.MyContextWrapper
import elektrogo.front.languages.Preference
import io.ktor.http.cio.websocket.*
import org.w3c.dom.Text

class PreferencesActivity : AppCompatActivity() {

    private lateinit var radiobuttonGroup: RadioGroup

    lateinit var preference: Preference
    lateinit var toolbar2 : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        val username : TextView = findViewById(R.id.usernameText)
        val email : TextView = findViewById(R.id.userMail)
        val deleteButton : Button = findViewById(R.id.deleteButton)

        username.text=SessionController.getUsername(this)
        email.text=SessionController.getEmail(this)

        toolbar2  = findViewById(R.id.toolbar_main)
        toolbar2.title = getString(R.string.ajustos)
        setSupportActionBar(toolbar2)

        radiobuttonGroup = findViewById(R.id.radiobuttongroup)

        preference = Preference(this)

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
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                1 -> {
                    preference.setLoginCount("ca")
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                2 -> {
                    preference.setLoginCount("es")
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
            }
        })

        deleteButton.setOnClickListener {

        }
    }
    override fun attachBaseContext(newBase: Context?) {
        preference = Preference(newBase!!)
        val lang = preference.getLoginCount()
        super.attachBaseContext(lang?.let { MyContextWrapper.wrap(newBase, it) })
    }
}