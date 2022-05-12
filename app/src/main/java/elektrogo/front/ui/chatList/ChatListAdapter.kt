package elektrogo.front.ui.chatList

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.Message
import elektrogo.front.ui.chatConversation.ChatConversation

class ChatListAdapter(private val context : Activity, private val chatList : ArrayList<String>)
    : ArrayAdapter<String>(context, R.layout.fragment_vehicle_list_item, chatList) {

    fun updateData(list: ArrayList<String>) {
        chatList.clear()
        chatList.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.fragment_chat_list_item, null)
        val viewModel = ChatListItemViewModel()
        val sessionController = SessionController

        val user : TextView = view.findViewById(R.id.listUser)
        val v = chatList[position]
        user.text = v

        val imageViewProfile : ImageView = view.findViewById(R.id.userImage)
        val imagePath = viewModel.getUsersProfilePhoto(user.text as String)
        if (imagePath != "null") Picasso.get().load(imagePath).into(imageViewProfile)


        view.setOnClickListener{
            val intent = Intent(context, ChatConversation::class.java).apply {
                val username : String = sessionController.getUsername(context)
                putExtra("userA", username)
                putExtra("userB", v)
            }
            context.startActivity(intent)
        }
        return view
    }
}