package elektrogo.front.ui.valorarUsuari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import elektrogo.front.MainActivity
import elektrogo.front.R

class ValorarUsuariActivity : AppCompatActivity() {
    private val valorarUsuariFragment = ValorarUsuariFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_valorar_usuari)
        loadFragment(valorarUsuariFragment)
    }

    //Listener del botó d'enrere de la barra d'Android
    override fun onBackPressed() {
        if (onBackPressedDispatcher.hasEnabledCallbacks()) {
            super.onBackPressed()
        } else {
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("origin", "vehicleList")
            startActivity(intent)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.commit()
    }
}