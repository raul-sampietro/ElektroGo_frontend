package elektrogo.front.ui.carPooling

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import elektrogo.front.R
import elektrogo.front.languages.MyContextWrapper
import elektrogo.front.languages.Preference

class TripsActivity : AppCompatActivity() {

    lateinit var Preference: Preference
    lateinit var toolbar2 : androidx.appcompat.widget.Toolbar
    lateinit var fragToLoad : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)
         fragToLoad = intent.getStringExtra("fragment").toString()
        if (fragToLoad == "NewCarPoolingFragment") {
            toolbar2  = findViewById(R.id.toolbar_main)
            toolbar2.title= getString(R.string.CrearTrajecte)
            setSupportActionBar(toolbar2)
            loadFragment(NewCarPoolingFragment())
        }
        else {
            loadFragment(MyTripsFragment())
            toolbar2  = findViewById(R.id.toolbar_main)
            toolbar2.title = getString(R.string.MyTrips)
            setSupportActionBar(toolbar2)
        }
    }

     fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.trips_container, fragment)
        transaction.commit()
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.catalan -> {
                Preference.setLoginCount("ca")
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
            R.id.spanish -> {
                Preference.setLoginCount("es")
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
            R.id.english -> {
                Preference.setLoginCount("en")
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        }
        return true
    }

    override fun attachBaseContext(newBase: Context?) {
        Preference = Preference(newBase!!)
        val lang = Preference.getLoginCount()
        super.attachBaseContext(lang?.let { MyContextWrapper.wrap(newBase, it) })
    }
}