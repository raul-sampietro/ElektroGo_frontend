package elektrogo.front.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import elektrogo.front.R
import elektrogo.front.ui.carPooling.NewCarPoolingFragment

class ProfileActivity : AppCompatActivity() {
    lateinit var toolbar2 : androidx.appcompat.widget.Toolbar
    lateinit var fragToLoad : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        toolbar2  = findViewById(R.id.toolbar_main)
        toolbar2.title= getString(R.string.perfil)
        setSupportActionBar(toolbar2)
        loadFragment(ProfileFragment())

    }

    fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.profile_container, fragment)
        transaction.commit()
    }

}