package elektrogo.front

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import elektrogo.front.databinding.ActivityMainBinding
import elektrogo.front.ui.Ruta.Ruta

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val ruta : Ruta = Ruta()

        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, ruta).commit()

    }
}