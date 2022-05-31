package elektrogo.front.ui.chatConversation

import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.Block
import elektrogo.front.model.Chat
import elektrogo.front.model.Message
import kotlinx.coroutines.runBlocking

class ChatConversationViewModel : ViewModel(){
    private lateinit var userA: String
    private lateinit var userB: String

    fun getConversation(userA: String, userB: String) : Pair<Int, ArrayList<Message>> = runBlocking {
        return@runBlocking FrontendController.getConversation(userA, userB)
    }

    fun sendMessage(sender: String, receiver: String, message: String) : Int  = runBlocking{
        return@runBlocking FrontendController.sendMessage(sender, receiver, message)
    }

    fun getUsersProfilePhoto(username: String): String = runBlocking {
        return@runBlocking FrontendController.getUserProfilePhoto(username)
    }

    fun getBlocks(username: String) : ArrayList<Block> = runBlocking {
        return@runBlocking FrontendController.getBlocks(username)
    }
}