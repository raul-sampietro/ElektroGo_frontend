package elektrogo.front.ui.chatConversation

import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.Chat
import elektrogo.front.model.Message
import kotlinx.coroutines.runBlocking

class ChatConversationViewModel : ViewModel(){
    private lateinit var userA: String
    private lateinit var userB: String

    fun getConversation(userA: String, userB: String) : ArrayList<Message> = runBlocking {
        FrontendController.getConversation(userA, userB)
    }
}