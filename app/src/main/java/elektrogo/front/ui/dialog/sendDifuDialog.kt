package elektrogo.front.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.ui.chatList.ChatListFragment
import elektrogo.front.ui.chatList.ChatListViewModel

class sendDifuDialog : DialogFragment() {

    private lateinit var viewModel: ChatListViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val selectedItems = ArrayList<Int>() // Where we track the selected items
            val correspondingItems = ArrayList<String>()
            val builder = AlertDialog.Builder(it)

            viewModel = ViewModelProvider(this)[ChatListViewModel::class.java]

            //get user ChatList

            val activeChats: ArrayList<String> = viewModel.getChatList(SessionController.getUsername(this.requireContext()))

            val list = arrayOfNulls<String>(activeChats.size)
            var i = 0
            for (value: String in activeChats) {
                list[i] = value
                ++i
            }

            // Set the dialog title
            builder.setTitle(R.string.pregunta_difusio)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(list, null,
                    DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            selectedItems.add(which)
                            correspondingItems.add(list[which].toString())
                        } else if (selectedItems.contains(which)) {
                            // Else, if the item is already in the array, remove it
                            selectedItems.remove(which)
                            correspondingItems.remove(list[which].toString())
                        }
                    })
                // Set the action buttons
                .setPositiveButton(R.string.next,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK, so save the selectedItems results somewhere
                        // or return them to the component that opened the dialog
                        val chatListFragment = ChatListFragment()
                        chatListFragment.postUsersDifuList(correspondingItems)

                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->

                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


}