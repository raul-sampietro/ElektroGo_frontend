package elektrogo.front.ui.chatConversation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import elektrogo.front.ChatService
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.Message
import elektrogo.front.ui.valorarUsuari.ValorarUsuariDialog


class ChatConversation : AppCompatActivity() {

    private lateinit var viewModel: ChatConversationViewModel
    private lateinit var conversation: ArrayList<Message>
    private lateinit var adapter: ChatConversationAdapter
    private lateinit var thread: Thread
    private lateinit var recyclerView: RecyclerView
    private var interrupted: Boolean = false
    //userA is the current user

    private fun changeData(newConversation: ArrayList<Message>) {
        conversation = newConversation
        adapter.updateData(conversation)
        recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
    }

    @SuppressLint("WrongViewCast", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {

        stopService(Intent(this, ChatService::class.java))

        interrupted = false

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

        val imageViewProfile : ImageView = findViewById(R.id.userImage)
        val imagePath = viewModel.getUsersProfilePhoto(nameUserB?.text as String)
        if (imagePath != "null") Picasso.get().load(imagePath).into(imageViewProfile)

        val userA = b?.getString("userA").toString()
        val userB = b?.getString("userB").toString()

        val sessionController = SessionController
        val currentUser = sessionController.getUsername(this)
        conversation = viewModel.getConversation(userA, userB)
        recyclerView = findViewById<RecyclerView>(R.id.listConversation)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ChatConversationAdapter(this, conversation, currentUser)
        recyclerView.adapter = adapter
        val position = adapter.itemCount - 1
        recyclerView.smoothScrollToPosition(position)


        thread = Thread {
            while(true and !interrupted) {
                Thread.sleep(2000)
                val conversationIncoming = viewModel.getConversation(userA, userB)
                if (conversationIncoming.size > conversation.size) {
                    runOnUiThread {
                        changeData(conversationIncoming)
                    }
                }
            }
        }
        thread.start()

        val backButton : ImageButton = findViewById(R.id.backButtonConversation)
        backButton.setOnClickListener {
            if (thread.isAlive) {
                interrupted = true
                startService(Intent(this, ChatService::class.java))
            }
                super.onBackPressed()
        }

        val editText : EditText = findViewById(R.id.messageToSend)
        editText.setOnClickListener {
            recyclerView.smoothScrollToPosition(position)
        }

        val sendButton : ImageButton = findViewById(R.id.sendMessage)
        sendButton.setOnClickListener {
            val text: EditText = findViewById(R.id.messageToSend)
            val message : String = text.text.toString()
            if (message != "") viewModel.sendMessage(userA, userB, message)
            text.text.clear()

            conversation = viewModel.getConversation(userA, userB)
            adapter.updateData(conversation)
            recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
        }

        val addMemberButton : ImageButton = findViewById(R.id.addMemberButton)

        addMemberButton.setOnClickListener {
            Log.i("add", "Le he dado click")
            val addMember = AddMemberDialog()
            val bundle = Bundle()
            bundle.putString("member", b!!.getString("userB"))
            addMember.arguments = bundle
            addMember.show( supportFragmentManager , "AddMemberDialog")
        }
    }
    override fun onBackPressed() {
        if (thread.isAlive) {
            interrupted = true
        }
        startService(Intent(this, ChatService::class.java))
        super.onBackPressed()
    }

}

