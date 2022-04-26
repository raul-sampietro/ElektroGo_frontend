package elektrogo.front.ui.chatList

import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.Chat
import kotlinx.coroutines.runBlocking

class ChatListViewModel : ViewModel() {
    private lateinit var string: String

    fun getChatList(username: String) : ArrayList<String> = runBlocking {
        FrontendController.getChatList(username)
    }
}