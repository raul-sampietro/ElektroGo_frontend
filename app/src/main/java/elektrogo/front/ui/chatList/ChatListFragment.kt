/**
 * @file ChatFragment.kt
 * @author Adria Abad
 * @date 15/04/2022
 * @brief Implementacio de la classe ChatFragment
 */
package elektrogo.front.ui.chatList


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.ui.chatConversation.ChatConversation
import elektrogo.front.ui.chatConversation.ChatConversationAdapter
import elektrogo.front.ui.dialog.sendDifuDialog

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
    private lateinit var difuUsers: ArrayList<String>


    private fun changeData(newChatList: ArrayList<String>) {
        chatList = newChatList
        adapter.updateData(chatList)
    }

    fun postUsersDifuList(list: ArrayList<String>) {
        difuUsers = list
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
        val username : String = sessionController.getUsername(view.context)
        chatList = viewModel.getChatList(username)

        adapter = ChatListAdapter(container?.context as Activity, chatList)
        listView.adapter = adapter

        interrupted = false

        thread = Thread {
            while (true and !interrupted) {
                Thread.sleep(5000)
                val chatListIncoming = viewModel.getChatList(username)
                if (chatListIncoming.size != chatList.size) {
                    activity?.runOnUiThread {
                        changeData(chatListIncoming)
                    }
                }
            }
        }
        thread.start()

        val searchButton: FloatingActionButton = view.findViewById(R.id.serachChat)
        searchButton.setOnClickListener {


        }

        val difuButton: FloatingActionButton = view.findViewById(R.id.sendDifu)
        difuButton.setOnClickListener {
            // first step: Choose the list of users
            val sendDifuFragment = sendDifuDialog()
            sendDifuFragment.show(this.parentFragmentManager, "game")
            //second step: Write the message

        }

        return view
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
