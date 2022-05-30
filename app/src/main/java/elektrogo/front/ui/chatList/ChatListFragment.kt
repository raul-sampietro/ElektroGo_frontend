/**
 * @file ChatFragment.kt
 * @author Adria Abad
 * @date 15/04/2022
 * @brief Implementacio de la classe ChatFragment
 */
package elektrogo.front.ui.chatList


import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.ui.carPooling.ListAdapterTrips
import elektrogo.front.ui.chatConversation.ChatConversation


/**
 * @brief La clase Chat representa la GUI de la pantalla on l'usuari veura el sistema de xat entre usuaris.
 */

class ChatListFragment() : Fragment() {

    companion object {
        fun newInstance() = ChatListFragment()
    }

    private lateinit var viewModel: ChatListViewModel
    private lateinit var chatList: ArrayList<String>
    private lateinit var adapter: ChatListAdapter
    private lateinit var thread: Thread
    private var interrupted: Boolean = false
    private lateinit var builder1: AlertDialog.Builder
    private lateinit var builder2: AlertDialog.Builder
    private lateinit var builder3: AlertDialog.Builder
    private var message: String = ""
    private var selectedItems = ArrayList<Int>() // Where we track the selected items
    private var correspondingItems = ArrayList<String>()
    private var username: String = ""


    private fun changeData(newChatList: ArrayList<String>) {
        chatList = newChatList
        adapter.updateData(chatList)
    }

    /**
     * @brief Metode que s'executa al crear el fragment.
     * @post Es crea la vista per al fragment.
     * @return Retorna el layout creat.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat_list, container, false)
        val listView: ListView = view.findViewById(R.id.list_chat_view)

        val sessionController = SessionController
        username = sessionController.getUsername(view.context)

        val result = viewModel.getChatList(username)
        if (result.first != 200) {
            Toast.makeText(context, R.string.errorChat, Toast.LENGTH_LONG).show()
            chatList = ArrayList<String>()
        }
        else {
            chatList = result.second
        }

        adapter = ChatListAdapter(container?.context as Activity, chatList)
        listView.adapter = adapter

        interrupted = false

        thread = Thread {
            while (true and !interrupted) {
                Thread.sleep(5000)
                val chatListIncoming = viewModel.getChatList(username)
                if (chatListIncoming.first == 200 && chatListIncoming.second.size != chatList.size) {
                    activity?.runOnUiThread {
                        changeData(chatListIncoming.second)
                    }
                }
            }
        }
        thread.start()

        builder1 = AlertDialog.Builder(this.context)
        builder2 = AlertDialog.Builder(this.context)
        builder3 = AlertDialog.Builder(this.context)

        val searchButton: FloatingActionButton = view.findViewById(R.id.serachChat)
        searchButton.setOnClickListener {
            configureBuilder3()
            builder3.show()
        }

        val difuButton: FloatingActionButton = view.findViewById(R.id.sendDifu)
        difuButton.setOnClickListener {
            configureBuilder1()
            builder1.show()
        }

        return view
    }

    private fun configureBuilder3() {
        builder3.setTitle(R.string.buscaUsuari)
        val input = EditText(this.context)
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder3.setView(input)

            // Set the action buttons
            .setPositiveButton(R.string.confirma,
                DialogInterface.OnClickListener { dialog, id ->
                    val userB = input.text.toString()
                    if (chatList.contains(userB)) {
                        val intent = Intent(this.context, ChatConversation::class.java).apply {
                            putExtra("userA", username)
                            putExtra("userB", userB)
                        }
                        this.context?.startActivity(intent)
                    }
                    else {
                        Toast.makeText(this.context, R.string.errorChatNotFound, Toast.LENGTH_LONG).show()
                    }
                })
            .setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialog, id ->
                })
    }

    private fun configureBuilder2() {
        builder2.setTitle(R.string.message_to_send)
        val input = EditText(this.context)
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder2.setView(input)

            // Set the action buttons
            .setPositiveButton(R.string.envia,
                DialogInterface.OnClickListener { dialog, id ->
                    message = input.text.toString()
                    sendDifusion(correspondingItems, message)
                })
            .setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialog, id ->
                    selectedItems.clear()
                    correspondingItems.clear()
                })
    }

    private fun sendDifusion(correspondingItems: java.util.ArrayList<String>, message: String) {
        for(item in correspondingItems) {
            viewModel.sendMessage(username, item, message)
        }
        Toast.makeText(this.context, R.string.message_sent, Toast.LENGTH_LONG).show()
    }

    private fun configureBuilder1() {

        val list = arrayOfNulls<String>(chatList.size)
        var i = 0
        for (value: String in chatList) {
            list[i] = value
            ++i
        }

        // Set the dialog title
        builder1.setTitle(R.string.pregunta_difusio)
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
                    configureBuilder2()
                    builder2.show()
                })
            .setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialog, id ->
                    selectedItems.clear()
                    correspondingItems.clear()
                })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(activity, ChatConversation::class.java)
        startActivity(intent)
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ChatListViewModel::class.java]
    }

    override fun onDestroyView() {
        if (thread.isAlive) {
            interrupted = true
        }
        super.onDestroyView()
    }
}
