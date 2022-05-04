/**
 * @file MainActivity.kt
 * @author Simón Helmuth Oliva
 * @brief Aquesta activity conté el menú navegable de barra inferior que permet carregar imostrar els fragments de les principals funcionalitats d'ElektroGo.
 */

package elektrogo.front

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.bottomnavigation.BottomNavigationView

import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import elektrogo.front.databinding.ActivityMainBinding
import elektrogo.front.ui.CarPooling.MyTripsFragment
import elektrogo.front.ui.Route.routeFragment
import elektrogo.front.ui.carPooling.filterTripsFragment
import elektrogo.front.ui.map.MapsFragment
import elektrogo.front.ui.profile.ProfileFragment
import elektrogo.front.ui.chatList.ChatListFragment

/**
 * @brief La classe MainActivity incorpora el menú principal i permet visualitzar els fragments de les funcionalitats principals d'ElektroGo.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //Configuració dels events clic
    private val mOnNavigationItemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.pooling -> {
                toolbar.title = "Pooling"

                val filtrarTrajectesFragment = filterTripsFragment()
                openFragment(filtrarTrajectesFragment)
                return@OnItemSelectedListener true
            }
            R.id.mapa -> {
                toolbar.title = "Mapa"
                //linia que havia escrit la Marina
                val mapsFragment /*: MapsFragment*/ = MapsFragment(this)
                openFragment(mapsFragment)
                return@OnItemSelectedListener true
            }
            R.id.ruta -> {
                toolbar.title = "Ruta"
                val routeFragment = routeFragment()
                openFragment(routeFragment)
                return@OnItemSelectedListener true
            }
            R.id.chat -> {
                //CANVIALO DANI XDXD
                //CANVIALO DANI XDXD
                //CANVIALO DANI XDXD
                toolbar.title = "Chat"
                val chatFragment = ChatListFragment()
                val myTripsFragment = MyTripsFragment()
                openFragment(myTripsFragment)
                return@OnItemSelectedListener true
            }
            R.id.perfil -> {
                toolbar.title = "Perfil"
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
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

}