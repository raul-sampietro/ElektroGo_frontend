package elektrogo.front.ui.chatConversation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import elektrogo.front.R

class MessageViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var message: TextView
    var sentAt: TextView

    init {
        message = v.findViewById(R.id.message)
        sentAt = v.findViewById(R.id.timestamp)
    }
}