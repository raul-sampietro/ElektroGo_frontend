package elektrogo.front.ui.preferences

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import elektrogo.front.R
import elektrogo.front.languages.Preference

class PreferencesActivity : AppCompatActivity() {

    private lateinit var radiobuttonGroup: RadioGroup

    lateinit var preference: Preference

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        radiobuttonGroup = findViewById(R.id.radiobuttongroup)

        context = this
        preference = Preference(context)

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
    }
}