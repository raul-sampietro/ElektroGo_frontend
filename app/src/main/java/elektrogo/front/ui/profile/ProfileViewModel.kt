package elektrogo.front.ui.profile

import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.RatingAvg
import kotlinx.coroutines.runBlocking

class ProfileViewModel : ViewModel() {
    fun getRating(username: String): Pair<Int, RatingAvg?> = runBlocking {
        return@runBlocking FrontendController.getRating(username)
    }

    fun getUsersProfilePhoto(username: String): String = runBlocking {
        return@runBlocking FrontendController.getUserProfilePhoto(username)
    }
    fun getDriver(username: String) = runBlocking {
        return@runBlocking FrontendController.getDriver(username)
    }
}