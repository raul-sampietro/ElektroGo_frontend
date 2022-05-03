/**
 * @file NewCarPoolingFragmentViewModel.kt
 * @author Joel Cardona
 * @date 03/05/2022
 * @brief Implementacio de la classe NewCarPoolingFragmentViewModel que s'encarrega de fer les crides amb el FrontendController
 */
package elektrogo.front.ui.CarPooling

import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.CarPooling
import elektrogo.front.model.Vehicle
import kotlinx.coroutines.runBlocking

/**
 * @brief La classe NewCarPoolingFragmentViewModel te assignats els metodes per fer les crides amb FrontendController.
 */
class NewCarPoolingFragmentViewModel : ViewModel() {
    /**
     * @brief S'encarrega de fer la crida a FrontendController corresponent per tal de rebre els vehicles de l'usuari logejat.
     * @param username string que conte el nom de l'usuari logejat a la app.
     * @pre
     * @return Retorna una ArrayList amb els vehicles que l'usuari te associats a backend.
     */
    fun getVehicleList(username: String) : ArrayList<Vehicle> = runBlocking {
        FrontendController.getVehicleList(username)
    }

    /**
     * @brief S'encarrega de fer la crida a FrontendController per tal de que aquest guardardi a backend la nova instancia de CarPooling
     * @param info Instancia de la classe serialitzable CarPooling que conte tota la informació del trajecte a crear
     * @pre info no es null.
     * @return Retorna el status code de corresponent de la crida HTTP.
     */
    fun saveCarpooling(info: CarPooling): Int = runBlocking{
        FrontendController.saveCarpooling(info)
    }
}