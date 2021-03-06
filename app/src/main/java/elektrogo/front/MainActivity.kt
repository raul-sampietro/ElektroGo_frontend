/**
 * @file MainActivity.kt
 * @author Simón Helmuth Oliva
 * @brief Aquesta activity conté el menú navegable de barra inferior que permet carregar imostrar els fragments de les principals funcionalitats d'ElektroGo.
 */

package elektrogo.front

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

import com.google.android.material.bottomnavigation.BottomNavigationView

import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import elektrogo.front.databinding.ActivityMainBinding
import elektrogo.front.languages.MyContextWrapper
import elektrogo.front.languages.Preference
import elektrogo.front.ui.route.RouteFragment
import elektrogo.front.ui.carPooling.FilterTripsFragment
import elektrogo.front.ui.map.MapsFragment
import elektrogo.front.ui.profile.ProfileFragment
import elektrogo.front.ui.chatList.ChatListFragment
import elektrogo.front.ui.preferences.PreferencesActivity

/**
 * @brief La classe MainActivity incorpora el menú principal i permet visualitzar els fragments de les funcionalitats principals d'ElektroGo.
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    lateinit var Preference: Preference
    lateinit var context: Context
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var toolbar2 : androidx.appcompat.widget.Toolbar


    //Configuració dels events clic
    private val mOnNavigationItemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.pooling -> {
                toolbar2.title = "Pooling"
                val filtrarTrajectesFragment = FilterTripsFragment()
                openFragment(filtrarTrajectesFragment)
                return@OnItemSelectedListener true
            }
            R.id.mapa -> {
                toolbar2.title = "Mapa"
                //linia que havia escrit la Marina
                val mapsFragment /*: MapsFragment*/ = MapsFragment(this)
                openFragment(mapsFragment)
                return@OnItemSelectedListener true
            }
            R.id.ruta -> {
                toolbar2.title = "Ruta"
                val routeFragment = RouteFragment()
                openFragment(routeFragment)
                return@OnItemSelectedListener true
            }
            R.id.chat -> {
                toolbar2.title = "Chat"
                val chatFragment = ChatListFragment()
                openFragment(chatFragment)
                return@OnItemSelectedListener true
            }
            R.id.perfil -> {
                toolbar2.title = "Perfil"
                val perfilFragment = ProfileFragment()
                openFragment(perfilFragment)
                return@OnItemSelectedListener true
            }
        }
        false
    }

    //Inicialització de la barra superior
    private lateinit var toolbar:ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        context = this
        Preference = Preference(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar2  = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar2)
        drawer = findViewById(R.id.main)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar2, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView : NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener)

        //Fem que la funcionalitat que se selecciona per defecte en obrir l'app sigui el mapa
        //Si es ve de la vista de VehicleList, el fragment seleccionat és el perfil
        if (intent.getStringExtra("origin").isNullOrBlank())
            bottomNavigation.selectedItemId = R.id.mapa
        else if (intent.getStringExtra("origin").equals("vehicleList"))
            bottomNavigation.selectedItemId = R.id.perfil
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
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
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }
    /**
     * @brief Metode per obrir un fragment.
     * @param fragment Fragment que es vol obrir.
     * @pre El fragment passat per paràmetre existeix.
     * @post Es carrega el fragment i es mostra al main container.
     */
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menubar, menu)
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