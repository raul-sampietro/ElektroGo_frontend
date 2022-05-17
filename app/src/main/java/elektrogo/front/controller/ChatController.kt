package elektrogo.front.controller

import elektrogo.front.model.Message
import kotlinx.coroutines.runBlocking

object ChatController {

    fun getReceivedMessages(username: String) = runBlocking {
        return@runBlocking FrontendController.getReceivedMessages(username)
    }

    fun getChatList(username: String) = runBlocking {
        return@runBlocking FrontendController.getChatList(username)
    }

    fun getConversation(userA: String, userB: String) : ArrayList<Message> = runBlocking {
        return@runBlocking FrontendController.getConversation(userA, userB)
    }

}