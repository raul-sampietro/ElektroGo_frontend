package elektrogo.front.ui.vehicleList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import elektrogo.front.MainActivity
import elektrogo.front.R

class VehicleListActivity : AppCompatActivity() {
    private val vehicleListFragment = VehicleListFragment()
    lateinit var toolbar2 : androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_list)
        toolbar2  = findViewById(R.id.toolbar_main)
        toolbar2.title= getString(R.string.llistaVehicles)
        setSupportActionBar(toolbar2)
        loadFragment(vehicleListFragment)
    }

    //Listener del botó d'enrere de la barra d'Android
    //Ho comento perque ja no fa falta
   /* override fun onBackPressed() {
        if (onBackPressedDispatcher.hasEnabledCallbacks()) {
            super.onBackPressed()
        } else {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }*/

    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment, "VehicleListFragment")
        transaction.commit()
    }

}