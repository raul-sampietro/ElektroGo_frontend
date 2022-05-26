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
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import elektrogo.front.R
import elektrogo.front.controller.session.SessionController
import elektrogo.front.ui.chatConversation.ChatConversation
import elektrogo.front.ui.chatConversation.ChatConversationAdapter

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

        object : CountDownTimer(120000, 6000) {
            override fun onTick(p0: Long) {
                chatList = viewModel.getChatList(username)
                adapter.updateData(chatList)
            }

            override fun onFinish() {
            }
        }.start()

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

}
