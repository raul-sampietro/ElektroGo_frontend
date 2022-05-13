package elektrogo.front.ui.carPooling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import elektrogo.front.R

class TripsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)
        var fragToLoad = intent.getStringExtra("fragment")
        if (fragToLoad == "NewCarPoolingFragment") loadFragment(NewCarPoolingFragment())
        else loadFragment(MyTripsFragment())
    }

     fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.commit()
    }
}