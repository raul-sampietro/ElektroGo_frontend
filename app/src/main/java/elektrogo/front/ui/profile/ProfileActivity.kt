package elektrogo.front.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.Block
import elektrogo.front.ui.carPooling.NewCarPoolingFragment
import elektrogo.front.ui.report.ReportUserFragment
import elektrogo.front.ui.valorarUsuari.ValorarUsuariDialog

class ProfileActivity : AppCompatActivity() {
    lateinit var toolbar2 : androidx.appcompat.widget.Toolbar
    lateinit var fragToLoad : String
    lateinit var user : String
    private var viewModel: ProfileViewModel = ProfileViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         user = intent.getStringExtra("username")!!
        setContentView(R.layout.activity_profile)
        toolbar2  = findViewById(R.id.toolbar_main)
        toolbar2.title= getString(R.string.perfil)
        toolbar2.setTitleTextColor(getColor(R.color.white))
        setSupportActionBar(toolbar2)
        if (user == SessionController.getUsername(this)) loadFragment(ProfileFragment())
        else user?.let { loadGuestFragment(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val blockedList : ArrayList<Block> = viewModel.getBlocks(user)
        var blocked = false
        for (block in blockedList) {
            if (block.userBlocking == SessionController.getUsername(this)) {
                blocked = true
            }
        }
        if (user != SessionController.getUsername(this) && !blocked) {
            var inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.menu_profile, menu)
            return true
        }
        else return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.valorar -> {
                Toast.makeText(this, "item1", Toast.LENGTH_LONG).show()
                val valorarDialog = ValorarUsuariDialog()

                val bundle = Bundle()
                bundle.putString("guestUser", user) //passem l'usuari que es vol valorar al dialog

                valorarDialog.arguments = bundle
                valorarDialog.show(supportFragmentManager, "confirmDialog")
            }
            R.id.denunciar -> {
                val bundle = Bundle()
                bundle.putString("guestUser", user) //passem l'usuari que es vol valorar al dialog
                val reportUserFragment = ReportUserFragment();
                reportUserFragment.arguments = bundle
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.profile_container, reportUserFragment)
                transaction.commit()
                Toast.makeText(this, "item2", Toast.LENGTH_LONG).show()

            }
            R.id.bloquejar -> {
                val userActual = SessionController.getUsername(this)
                if (user != null) {
                    viewModel.blockUser(userActual, user)
                }
                loadGuestFragment(user)
                Toast.makeText(this, "item3", Toast.LENGTH_LONG).show()
            }
        }
        return true
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