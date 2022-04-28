package elektrogo.front.ui.chatConversation

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import elektrogo.front.R
import elektrogo.front.model.Message


class ChatConversation : AppCompatActivity() {

    private lateinit var viewModel: ChatConversationViewModel
    private lateinit var conversation: ArrayList<Message>
    private lateinit var adapter: ChatConversationAdapter


    @SuppressLint("WrongViewCast", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ChatConversationViewModel::class.java]
        setContentView(R.layout.activity_chat_conversation)

        val b = intent.extras
        val nameUserB : TextView? = findViewById(R.id.NameofUserB)
        if (nameUserB != null) {
            if (b != null) {
                nameUserB.text = b.getString("userB")
            }
        }

        conversation = viewModel.getConversation(b?.getString("userA").toString(), b?.getString("userB").toString())
        val recyclerView = findViewById<RecyclerView>(R.id.listConversation)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ChatConversationAdapter(this, conversation)
        recyclerView.adapter = adapter


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

