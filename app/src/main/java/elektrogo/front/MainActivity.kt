package elektrogo.front

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import elektrogo.front.databinding.ActivityMainBinding
import elektrogo.front.ui.Ruta.RutaFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val rutaFragment : RutaFragment = RutaFragment(this)

        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, rutaFragment).commit()

    }
}