package elektrogo.front

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class TestingServerConnection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.testing_server_connection)

        var text: TextView = findViewById(R.id.textView)

        lifecycleScope.launch{ //No li agrada
            val string = getText()
            text.setText(string)
        }
    }

    private suspend fun getText(): String{
        return FrontendController.doGetTest()
    }
}