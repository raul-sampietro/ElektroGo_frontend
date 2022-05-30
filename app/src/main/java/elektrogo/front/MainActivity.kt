/**
 * @file MainActivity.kt
 * @author Simón Helmuth Oliva
 * @brief Aquesta activity conté el menú navegable de barra inferior que permet carregar imostrar els fragments de les principals funcionalitats d'ElektroGo.
 */

package elektrogo.front

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout

import com.google.android.material.bottomnavigation.BottomNavigationView

import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import elektrogo.front.controller.FrontendController
import elektrogo.front.controller.session.SessionController
import elektrogo.front.databinding.ActivityMainBinding
import elektrogo.front.languages.MyContextWrapper
import elektrogo.front.languages.Preference
import elektrogo.front.ui.route.RouteFragment
import elektrogo.front.ui.carPooling.FilterTripsFragment
import elektrogo.front.ui.carPooling.TripsActivity
import elektrogo.front.ui.map.MapsFragment
import elektrogo.front.ui.chatList.ChatListFragment
import elektrogo.front.ui.login.LoginActivity
import elektrogo.front.ui.profile.ProfileActivity
import elektrogo.front.ui.vehicleList.VehicleListActivity
import kotlinx.coroutines.runBlocking

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
    lateinit var currentFragment: String

    //configura les notificacions del sistema
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channelId = "ChatChannel"
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    //Configuració dels events clic
    private val mOnNavigationItemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.pooling -> {
                toolbar2.title = "Pooling"
                val filtrarTrajectesFragment = FilterTripsFragment()
                currentFragment = "FilterTripsFragment"
                openFragment(filtrarTrajectesFragment)
                return@OnItemSelectedListener true
            }
            R.id.mapa -> {
                toolbar2.title = getString(R.string.ToolbarMapa)
                //linia que havia escrit la Marina
                val mapsFragment /*: MapsFragment*/ = MapsFragment(this)
                currentFragment = "MapsFragment"
                openFragment(mapsFragment)
                return@OnItemSelectedListener true
            }
            R.id.ruta -> {
                toolbar2.title = getString(R.string.ToolbarRuta)
                val routeFragment = RouteFragment()
                currentFragment = "RouteFragment"
                openFragment(routeFragment)
                return@OnItemSelectedListener true
            }
            R.id.chat -> {
                toolbar2.title = getString(R.string.ToolbarChat)
                val chatFragment = ChatListFragment()
                currentFragment= "ChatListFragment"
                openFragment(chatFragment)
                return@OnItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        context = this
        Preference = Preference(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar2  = findViewById(R.id.toolbar_main)
        toolbar2.setTitleTextColor(getColor(R.color.white))
        setSupportActionBar(toolbar2)
        drawer = findViewById(R.id.main)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar2, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView : NavigationView = findViewById(R.id.nav_view)
        val header : View = navigationView.getHeaderView(0)
        val profileImage : ImageView = header.findViewById(R.id.profile_imageNav)
        val usernameText : TextView = header.findViewById(R.id.usernameNav)
        val clickeableLayout :LinearLayout = header.findViewById(R.id.headerProfile)
        if (SessionController.getImageUrl(this)!="null") {
            Picasso.get().load(SessionController.getImageUrl(this)).into(profileImage)
        }
        else profileImage.setImageResource(R.drawable.avatar)
        usernameText.text= SessionController.getUsername(this)
        navigationView.setNavigationItemSelectedListener(this)

        clickeableLayout.setOnClickListener {
            var i  = Intent(this, ProfileActivity::class.java)
            i.putExtra("username", SessionController.getUsername(this))
            startActivity(i)
        }

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener)

        //Fem que la funcionalitat que se selecciona per defecte en obrir l'app sigui el mapa
        //Si es ve de la vista de VehicleList, el fragment seleccionat és el perfil
        if (intent.getStringExtra("origin").isNullOrBlank()) {
            bottomNavigation.selectedItemId = R.id.mapa
            currentFragment = "MapsFragment"
            toolbar2.title = getString(R.string.title_map)
        }
        else if (intent.getStringExtra("origin").equals("MapsFragment")) {
            bottomNavigation.selectedItemId = R.id.mapa
            currentFragment = "MapsFragment"
            toolbar2.title = getString(R.string.title_map)

        }
        else if (intent.getStringExtra("origin").equals("RouteFragment")) {
            bottomNavigation.selectedItemId = R.id.ruta
            currentFragment = "RouteFragment"
            toolbar2.title = getString(R.string.title_route)

        }
        else if (intent.getStringExtra("origin").equals("FilterTripsFragment")) {
            bottomNavigation.selectedItemId = R.id.pooling
            currentFragment = "FilterTripsFragment"
            toolbar2.title = getString(R.string.title_pooling)

        }
        else if (intent.getStringExtra("origin").equals("ChatListFragment")) {
            bottomNavigation.selectedItemId = R.id.chat
            currentFragment = "ChatListFragment"
            toolbar2.title = getString(R.string.ToolbarChat)

        }


        //Iniciem el service que envia notificacions per al xat
        createNotificationChannel()
        Intent(this, ChatService::class.java).also { intent ->
            startService(intent)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_one -> {
                var i  = Intent(this, ProfileActivity::class.java)
                i.putExtra("username", SessionController.getUsername(this))
                startActivity(i)
            }
            R.id.nav_item_two -> {
                val intent = Intent(this, TripsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_item_three -> {
                val intent = Intent(this, VehicleListActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_item_four -> {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build()
                val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                    mGoogleSignInClient.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                SessionController.setCurrentSession(null)
                Toast.makeText(this, getString(R.string.signOutSuccesfull), Toast.LENGTH_SHORT).show()
                this.finish()

            }
            R.id.nav_item_seven ->{
                val alertDialog: AlertDialog? = this.let {
                    val builder = AlertDialog.Builder(it)
                    // TODO 3 hardcoded strings
                    builder.setMessage(getString(R.string.deleteAccountVerification))
                    builder.apply {
                        setPositiveButton("SI",
                            DialogInterface.OnClickListener { dialog, id ->
                                val status = runBlocking { FrontendController.deleteUser(SessionController.getUsername(context)) }
                                if (status != 200)  Toast.makeText(context,getString(R.string.errorDeleteAccount), Toast.LENGTH_SHORT).show()
                                else {
                                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestEmail()
                                        .build()
                                    val mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
                                    mGoogleSignInClient.signOut()
                                    val intent = Intent(context, LoginActivity::class.java)
                                    startActivity(intent)
                                    SessionController.setCurrentSession(null)
                                    Toast.makeText(context, getString(R.string.deleteAccountOk), Toast.LENGTH_SHORT).show()
                                    finish();
                                }
                            })
                        setNegativeButton("NO",
                            DialogInterface.OnClickListener { dialog, id ->
                                Toast.makeText(context, getString(R.string.noDeleteAccount), Toast.LENGTH_LONG).show()
                            })
                    }
                    builder.create()
                }
                alertDialog?.show()
            }

        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setLanguage(lang: String) {
        Preference.setLoginCount(lang)
        finish();
        overridePendingTransition(0, 0);
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("origin", currentFragment)
        Log.i("CurrentFragment", currentFragment)
        startActivity(intent);
        overridePendingTransition(0, 0);
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
                setLanguage("ca")
            }
            R.id.spanish -> {
                setLanguage("es")
            }
            R.id.english -> {
                setLanguage("en")
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