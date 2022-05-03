package elektrogo.front.ui.chatConversation

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import elektrogo.front.R
import elektrogo.front.model.Message


class ChatConversationAdapter internal constructor(context: Context?, data: ArrayList<Message>,
                                                   var currentUser: String
) :
    RecyclerView.Adapter<ChatConversationAdapter.ViewHolder>() {
    private val mData: ArrayList<Message>
    private val mInflater: LayoutInflater

    private var messageSent = 0
    private var messageReceived = 1

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(messages: ArrayList<Message>) {
        mData.clear()
        mData.addAll(messages)
        notifyDataSetChanged()
    }

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View
        if (viewType == messageSent) {
            view = mInflater.inflate(R.layout.sender_message, parent, false)
        }
        else {
            view = mInflater.inflate(R.layout.receiver_message, parent, false)
        }
        return ViewHolder(view)
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = mData[position].message
        holder.timestampText.text = mData[position].sentAt?.substring(5, 16)
    }

    // total number of rows
    override fun getItemCount(): Int {
        return mData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text: TextView
        var timestampText: TextView

        init {
            text = itemView.findViewById(R.id.message)
            timestampText = itemView.findViewById(R.id.timestamp)
        }
    }

    // data is passed into the constructor
    init {
        mInflater = LayoutInflater.from(context)
        mData = data
    }

    override fun getItemViewType(position: Int): Int {
        val message : Message = mData[position]
        return if (message.sender == currentUser) {
            messageSent
        } else {
            messageReceived
        }
    }
}

