package elektrogo.front

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.bottomnavigation.BottomNavigationView

import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import elektrogo.front.databinding.ActivityMainBinding
import elektrogo.front.ui.Route.routeFragment
import elektrogo.front.ui.map.MapsFragment
import elektrogo.front.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //Configuració dels events clic
    private val mOnNavigationItemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.pooling -> {
                toolbar.title = "Pooling"
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
        bottomNavigation.selectedItemId = R.id.mapa
    }

    //Carrega el fragment passat per paràmetre al main_container
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}