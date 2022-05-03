package elektrogo.front.ui.profile

import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import kotlinx.coroutines.runBlocking

class ProfileViewModel : ViewModel() {
    fun getRating(username: String): Pair<Int, Double> = runBlocking {
        return@runBlocking FrontendController.getRating(username)
    }
    // TODO: Implement the ViewModel
}