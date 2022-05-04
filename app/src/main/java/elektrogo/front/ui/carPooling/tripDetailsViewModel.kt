/**
 * @file tripDetailsViewModel.java
 * @author Marina Alapont
 * @date 1/05/2022
 * @brief Implementacio d'un view model per tal de fer la comunicacio entre la activity i el front controller.
 */
package elektrogo.front.ui.carPooling

import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.RatingAvg
import kotlinx.coroutines.runBlocking

/**
 * @brief La clase tripDetailsViewModel s'encarrega de la comunicacio de la GUI amb el front controller.
 */
class tripDetailsViewModel : ViewModel() {

    /**
     * @brief Metode que es comunica amb FrontendController per tal d'obtenir la valoracio mitjana d'un usuari.
     * @param username nom d'usuari del usuari per el que volem la valoracio mitjana.
     * @return Retorna un Pair<Int,Double> on el int es el code status i RatingAvg la valoracio mitjana de l'usuari i el nombre de valoracions que ha rebut.
     */

    fun getRating(username: String): Pair<Int, RatingAvg?> = runBlocking {
        return@runBlocking FrontendController.getRating(username)
    }

    /**
     * @brief Metode que es comunica amb FrontendController per tal d'obtenir el path o uri de la fotografia de perfil d'un usuari.
     * @param username nom d'usuari del usuari per el que volem la imatge de perfil.
     * @return Retorna un String que es el path de la imatge de perfil de l'usuari per el qual l'hem demanat.
     */
    fun getUsersProfilePhoto(username: String): String = runBlocking {
        return@runBlocking FrontendController.getUserProfilePhoto(username)
    }

}