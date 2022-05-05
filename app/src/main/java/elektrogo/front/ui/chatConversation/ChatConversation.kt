package elektrogo.front.ui.chatConversation

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.Message


class ChatConversation : AppCompatActivity() {

    private lateinit var viewModel: ChatConversationViewModel
    private lateinit var conversation: ArrayList<Message>
    private lateinit var adapter: ChatConversationAdapter

    //userA is the current user

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

        val imageViewProfile : ImageView = findViewById(R.id.userImage)
        val imagePath = viewModel.getUsersProfilePhoto(nameUserB?.text as String)
        if (imagePath != "null") Picasso.get().load(imagePath).into(imageViewProfile)

        val userA = b?.getString("userA").toString()
        val userB = b?.getString("userB").toString()

        val sessionController = SessionController
        val currentUser = sessionController.getUsername(this)
        conversation = viewModel.getConversation(userA, userB)
        val recyclerView = findViewById<RecyclerView>(R.id.listConversation)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ChatConversationAdapter(this, conversation, currentUser)
        recyclerView.adapter = adapter
        val position = adapter.itemCount - 1
        recyclerView.smoothScrollToPosition(position)

        // TODO -> arreglar CountDownTimer para que no se acabe
        object : CountDownTimer(180000, 1500) {
            override fun onTick(p0: Long) {
                conversation = viewModel.getConversation(userA, userB)
                adapter.updateData(conversation)
            }

            override fun onFinish() {
            }
        }.start()

        val backButton : ImageButton = findViewById(R.id.backButtonConversation)
        backButton.setOnClickListener {
            finish()
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

            /*
            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
             */

            conversation = viewModel.getConversation(userA, userB)
            adapter.updateData(conversation)
            recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
        }
    }

}

