package elektrogo.front.ui.chatList

import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.Chat
import elektrogo.front.model.Vehicle
import kotlinx.coroutines.runBlocking

class ChatListViewModel {
    private lateinit var string: String

    fun getChatList(username: String) : ArrayList<Chat> = runBlocking {
        FrontendController.getChatList(username)
    }
}