package elektrogo.front.ui.vehicleList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import elektrogo.front.MainActivity
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.Driver

class VehicleListActivity : AppCompatActivity() {
    private val vehicleListFragment = VehicleListFragment()

    private val verifyingDriverFragment = VerifyingDriverFragment()

    private val formNewDriverFragment = NewDriverFragment()

    lateinit var toolbar2 : androidx.appcompat.widget.Toolbar

    private val viewModel = VehicleListViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_list)
        toolbar2  = findViewById(R.id.toolbar_main)
        toolbar2.title= getString(R.string.llistaVehicles)
        toolbar2.setTitleTextColor(getColor(R.color.white))
        setSupportActionBar(toolbar2)


        var httpResponse : Pair<Int, Driver?> = viewModel.getDriver(SessionController.getUsername(this))
        if (httpResponse.first == 432) loadFragment(formNewDriverFragment)
        else if (httpResponse.first == 200 && httpResponse.second!!.status == "pendent") loadFragment(verifyingDriverFragment)
        else if (httpResponse.first == 200 && httpResponse.second!!.status == "verified") loadFragment(vehicleListFragment)
        else {
            Toast.makeText(this, resources.getString(R.string.VehicleCreatedSuccessfully), Toast.LENGTH_LONG).show()
        }

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