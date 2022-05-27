
package elektrogo.front.ui.preferences

import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import kotlinx.coroutines.runBlocking


class PreferencesViewModel : ViewModel() {

    fun deleteAccount(username: String) : Int = runBlocking {
        return@runBlocking FrontendController.deleteUser(username)
    }

}