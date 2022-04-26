package elektrogo.front.ui.chatConversation

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import elektrogo.front.R


class ChatConversation : AppCompatActivity() {

    private lateinit var toolbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_conversation)

        val b = intent.extras
        val nameUserB : TextView? = findViewById(R.id.NameofUserB)
        if (nameUserB != null) {
            if (b != null) {
                nameUserB.text = b.getString("userB")
            }
        }


        val backButton : ImageButton = findViewById(R.id.backButtonConversation)
        backButton.setOnClickListener {
            finish()
        }

        val sendButton : ImageButton = findViewById(R.id.sendMessage)
        sendButton.setOnClickListener {
            val text : EditText = findViewById(R.id.message)
            // send text
        }
    }

}

