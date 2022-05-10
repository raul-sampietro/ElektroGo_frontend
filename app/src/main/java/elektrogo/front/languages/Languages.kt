package elektrogo.front.languages

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import elektrogo.front.R
import java.util.*

class Languages : AppCompatActivity() {

    lateinit var Preference: Preference
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proba_languages)
        context = this
        Preference = Preference(this)
        val englishButton : Button = this.findViewById(R.id.english)
        val spanishButton : Button = this.findViewById(R.id.spanish)
        val catalanButton : Button = this.findViewById(R.id.catalan)

        englishButton.setOnClickListener {
            Preference.setLoginCount("en")
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
        catalanButton.setOnClickListener {
            Preference.setLoginCount("ca")
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
        spanishButton.setOnClickListener {
            Preference.setLoginCount("es")
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }


    }
    //function from ContextWrapper.class
    override fun attachBaseContext(newBase: Context?) {
        Preference = Preference(newBase!!)
        val lang = Preference.getLoginCount()
        super.attachBaseContext(lang?.let { MyContextWrapper.wrap(newBase, it) })
    }
}