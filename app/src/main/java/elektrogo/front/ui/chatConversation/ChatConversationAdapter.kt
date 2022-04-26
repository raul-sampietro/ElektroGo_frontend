package elektrogo.front.ui.chatConversation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import elektrogo.front.R
import elektrogo.front.model.Message

class ChatConversationAdapter     // Provide a suitable constructor (depends on the kind of dataset)
    (  // The items to display in your RecyclerView
    private val messages: List<Message>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val SENT = 0
    private val RECEIVED = 1

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return messages.size
    }

    // Returns the view type of the item at position for the purposes of view recycling.
    override fun getItemViewType(position: Int): Int {
        // TODO ADD CURRENT USER NAME
        if (messages[position].sender == "Test2") {
            return SENT
        } else {
            return RECEIVED
        }
    }

    /**
     * This method creates different RecyclerView.ViewHolder objects based on the item view type.\
     *
     * @param viewGroup ViewGroup container for the item
     * @param viewType type of view to be inflated
     * @return viewHolder to be inflated
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(viewGroup.context)
        when (viewType) {
            SENT -> {
                val v1: View = inflater.inflate(R.layout.sended_message, viewGroup, false)
                viewHolder = MessageViewHolder(v1)
            }
            else -> {
                val v2: View = inflater.inflate(R.layout.received_message, viewGroup, false)
                viewHolder = MessageViewHolder(v2)
            }
        }
        return viewHolder
    }

    /**
     * This method internally calls onBindViewHolder(ViewHolder, int) to update the
     * RecyclerView.ViewHolder contents with the item at the given position
     * and also sets up some private fields to be used by RecyclerView.
     *
     * @param viewHolder The type of RecyclerView.ViewHolder to populate
     * @param position Item position in the viewgroup.
     */
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            SENT -> {
                val vh1 = viewHolder as MessageViewHolder
                configureViewHolder(vh1, position)
            }
            else -> {
                val vh2 = viewHolder as MessageViewHolder
                configureViewHolder(vh2, position)
            }
        }
    }


    private fun configureViewHolder(vh: MessageViewHolder, position: Int) {
        val message = messages[position]
        vh.message.text = message.message
        vh.sentAt.text = message.sentAt
    }
}