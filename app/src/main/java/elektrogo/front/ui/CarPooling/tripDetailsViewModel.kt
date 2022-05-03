/**
 * @file tripDetailsViewModel.java
 * @author Marina Alapont
 * @date 1/05/2022
 * @brief Implementacio d'un view model per tal de fer la comunicació entre la activity i el front controller.
 */
package elektrogo.front.ui.carPooling

import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import kotlinx.coroutines.runBlocking

/**
 * @brief La clase tripDetailsViewModel s'encarrega de la comunicació de la GUI amb el front controller.
 */
class tripDetailsViewModel : ViewModel() {

    fun getRating(username: String): Pair<Int, Double> = runBlocking {
        return@runBlocking FrontendController.getRating(username)
    }

    fun getUsersProfilePhoto(username: String): String = runBlocking {
        return@runBlocking FrontendController.getUserProfilePhoto(username)
    }

}