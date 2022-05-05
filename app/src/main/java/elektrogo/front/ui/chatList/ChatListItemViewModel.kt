package elektrogo.front.ui.chatList

import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import kotlinx.coroutines.runBlocking

class ChatListItemViewModel : ViewModel() {

    fun getUsersProfilePhoto(username: String): String = runBlocking {
        return@runBlocking FrontendController.getUserProfilePhoto(username)
    }
}