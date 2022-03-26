/**
 * @file AddVehicleModelView.kt
 * @author Joel Cardona
 * @brief Implementacio de la classe AddVehicleModelView
 */

package elektrogo.front.ui.vehicleAdd

import android.graphics.Bitmap
import android.text.Editable
import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.Vehicle
import kotlinx.coroutines.runBlocking

/**
 * @brief La classe AddVehicleModelView te assignats els metodes que no depenen de la interficie d'AddVehicle, aixi com comprovar si una matricula es correcta com fer les crides amb backend.
 */
public class AddVehicleModelView : ViewModel() {
    /**
     * @brief Comprova que una si licensePlateToCheck es una matricula valida o no.
     * @param licensePlateToCheck Editable que conte la matricula a comprovar
     * @pre licensePlateToCheck no es null
     * @return Retorna cert si licensePlateToCheck es una matricula valida. Fals si no ho es.
     */
    fun checkLicensePlate(licensePlateToCheck: Editable): Boolean {
        var isValid = true
        for(i in licensePlateToCheck.indices){   //Check licensePlate has valid input
            if (licensePlateToCheck.length == 7) { //Then it is a car
                if (i < 4 && licensePlateToCheck[i] !in '0'..'9') {
                    isValid = false
                } else if (i >= 4 && (licensePlateToCheck[i] !in 'A'..'Z' && licensePlateToCheck[i] !in 'a'..'z')) {
                    isValid = false
                }
            }
            else { //Then it is a moped
                if (i in 1..4 && licensePlateToCheck[i] !in '0'..'9') {
                    isValid = false
                } else if ((i >= 5 || i == 0) && (licensePlateToCheck[i] !in 'A'..'Z' && licensePlateToCheck[i] !in 'a'..'z')) {
                    isValid = false
                }
            }
        }
        return isValid
    }

    /**
     * @brief S'encarrega de fer les crides a FrontendController corresponents per tal d'enviar la informacio del vehicle i la imatge d'aquest.
     * @param vehicleInfo instancia de la data class serialitzable Vehicle amb la informacio del vehicle que l'usuari ha afegit
     * @param bitmapVehiclePic bitmap amb la imatge del vehicle corresponent al vehicleInfo.
     * @pre vehicleInfo te tots els camps del vehicle complerts excepte la imatge i bitmapVehiclePic no es null
     * @return Retorna el statusCode de la primera crida rebuda d'executar la crida amb FrontendController.
     */
    fun sendVehicleInfo(vehicleInfo: Vehicle, bitmapVehiclePic: Bitmap): Int  = runBlocking{
        val statusCode  = FrontendController.sendVehicleInfo(vehicleInfo)
        if(statusCode in 200..299) FrontendController.sendVehiclePhoto(vehicleInfo.numberPlate, bitmapVehiclePic) //Nomes es pot fer aquesta crida si l'anterior ha anat be
        return@runBlocking statusCode
    }


}