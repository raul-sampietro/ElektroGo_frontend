package elektrogo.front.ui.chatList

import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import kotlinx.coroutines.runBlocking

class ChatListViewModel : ViewModel() {
    private lateinit var string: String

    fun getChatList(username: String) : ArrayList<String> = runBlocking {
        return@runBlocking FrontendController.getChatList(username)
    }

    fun sendMessage(sender: String, receiver: String, message: String) = runBlocking {
        return@runBlocking FrontendController.sendMessage(sender, receiver, message)
    }

}