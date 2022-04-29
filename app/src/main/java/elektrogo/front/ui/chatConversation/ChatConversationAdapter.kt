package elektrogo.front.ui.chatConversation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import elektrogo.front.R
import elektrogo.front.model.Message

class ChatConversationAdapter internal constructor(context: Context?, data: ArrayList<Message>) :
    RecyclerView.Adapter<ChatConversationAdapter.ViewHolder>() {
    private val mData: ArrayList<Message>
    private val mInflater: LayoutInflater

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.chat_message, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = mData[position].message
        holder.userText.text = mData[position].sender
        holder.timestampText.text = mData[position].sentAt
    }

    // total number of rows
    override fun getItemCount(): Int {
        return mData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text: TextView
        var userText: TextView
        var timestampText: TextView

        init {
            text = itemView.findViewById(R.id.message)
            userText = itemView.findViewById(R.id.userMessage)
            timestampText = itemView.findViewById(R.id.timestamp)
        }
    }

    // data is passed into the constructor
    init {
        mInflater = LayoutInflater.from(context)
        mData = data
    }
}

