package elektrogo.front.ui.profile

import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.Achievement
import elektrogo.front.model.Block
import elektrogo.front.model.RatingAvg
import kotlinx.coroutines.runBlocking

class ProfileViewModel : ViewModel() {
    fun getRating(username: String): Pair<Int, RatingAvg?> = runBlocking {
        return@runBlocking FrontendController.getRating(username)
    }

    fun getUsersProfilePhoto(username: String): String = runBlocking {
        return@runBlocking FrontendController.getUserProfilePhoto(username)
    }

    fun getDriver(username: String) : Boolean = runBlocking {
        return@runBlocking FrontendController.getDriver(username)
    }

    fun getBlocks(username: String) : ArrayList<Block> = runBlocking {
        return@runBlocking FrontendController.getBlocks(username)
    }

    fun getAchievement(achievement: String, username: String) : Achievement = runBlocking {
        return@runBlocking FrontendController.getAchievement(achievement, username)
    }

    fun deleteUser(username: String) : Int = runBlocking {
        return@runBlocking FrontendController.deleteUser(username)
    }

    fun blockUser(username: String, blockUser: String) : Boolean = runBlocking {
        return@runBlocking FrontendController.Block(username, blockUser)
    }
}