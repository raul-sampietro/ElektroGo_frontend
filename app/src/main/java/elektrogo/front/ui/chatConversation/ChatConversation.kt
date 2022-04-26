package elektrogo.front.ui.chatConversation

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import elektrogo.front.R
import elektrogo.front.model.Chat
import elektrogo.front.model.Message
import elektrogo.front.ui.chatList.ChatListViewModel


class ChatConversation : AppCompatActivity() {

    private lateinit var viewModel: ChatConversationViewModel
    private lateinit var conversation: ArrayList<Message>

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

        val recycleView : RecyclerView = findViewById(R.id.recyclerviewofConversation)

        //val listMessages =
        //recycleView.adapter = ChatConversationAdapter(listMessages)

        conversation = viewModel.getConversation(b?.getString("userA").toString(), b?.getString("userB").toString())
        recycleView.adapter = ChatConversationAdapter(conversation)


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

