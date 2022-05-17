package elektrogo.front.controller

import kotlinx.coroutines.runBlocking

object ChatController {

    fun getReceivedMessages(username: String) = runBlocking {
        return@runBlocking FrontendController.getReceivedMessages(username)
    }

    fun getUsersProfilePhoto(username: String): String = runBlocking {
        return@runBlocking FrontendController.getUserProfilePhoto(username)
    }

}