package elektrogo.front.ui.chatConversation

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import elektrogo.front.R


class ChatConversation : AppCompatActivity() {

    private lateinit var toolbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val nameUserB : TextView? = findViewById(R.id.NameofUserB)
        nameUserB?.text ?:  "UserB"
        setContentView(R.layout.activity_chat_conversation)
    }

    override fun onStart() {
        super.onStart()

    }
}