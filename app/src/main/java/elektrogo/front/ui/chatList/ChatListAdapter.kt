package elektrogo.front.ui.chatList

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import elektrogo.front.R
import elektrogo.front.model.Chat
import elektrogo.front.ui.chatConversation.ChatConversation

class ChatListAdapter(private val context : Activity, private val chatList : ArrayList<Chat>)
    : ArrayAdapter<Chat>(context, R.layout.fragment_vehicle_list_item, chatList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.fragment_chat_list_item, null)
        // TODO imageView
        val user : TextView = view.findViewById(R.id.listUser)

        val v = chatList[position]
        user.text = v.userB

        view.setOnClickListener{
            val intent = Intent(context, ChatConversation::class.java).apply {
                putExtra("chat", v.userA)
                putExtra("userB", v.userB)
                putExtra("messages", v.messages)
            }
            //TODO añadir current user
            context.startActivity(intent)
        }
        return view
    }
}