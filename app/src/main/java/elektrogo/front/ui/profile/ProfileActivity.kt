package elektrogo.front.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.ui.carPooling.NewCarPoolingFragment

class ProfileActivity : AppCompatActivity() {
    lateinit var toolbar2 : androidx.appcompat.widget.Toolbar
    lateinit var fragToLoad : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = intent.getStringExtra("username")
        setContentView(R.layout.activity_profile)
        toolbar2  = findViewById(R.id.toolbar_main)
        toolbar2.title= getString(R.string.perfil)
        toolbar2.setTitleTextColor(getColor(R.color.white))
        setSupportActionBar(toolbar2)
        if (user == SessionController.getUsername(this)) loadFragment(ProfileFragment())
        else user?.let { loadGuestFragment(it) }
    }

    fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.profile_container, fragment)
        transaction.commit()
    }

    fun loadGuestFragment(user: String) {
        val bundle = Bundle()
        bundle.putString("username", user)

        val fragmentGuest = GuestProfileFragment()

        fragmentGuest.arguments = bundle

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.profile_container, fragmentGuest)
        transaction.commit()
    }

}