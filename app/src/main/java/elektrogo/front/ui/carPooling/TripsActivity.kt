package elektrogo.front.ui.carPooling

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import elektrogo.front.R
import elektrogo.front.languages.MyContextWrapper
import elektrogo.front.languages.Preference
import elektrogo.front.ui.preferences.PreferencesActivity

class TripsActivity : AppCompatActivity() {

    lateinit var Preference: Preference
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
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
            drawer = findViewById(R.id.tripsAct)
            toggle = ActionBarDrawerToggle(this, drawer, toolbar2, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            drawer.addDrawerListener(toggle)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
        }
    }

     fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.trips_container, fragment)
        transaction.commit()
    }

     fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_one -> {
                var i : Intent = Intent(this, PreferencesActivity::class.java)
                startActivity(i)
            }

        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (fragToLoad != "NewCarPoolingFragment") toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (fragToLoad != "NewCarPoolingFragment") toggle.onConfigurationChanged(newConfig)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (fragToLoad != "NewCarPoolingFragment") {
            var inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.menubar, menu)
        }
        return true
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