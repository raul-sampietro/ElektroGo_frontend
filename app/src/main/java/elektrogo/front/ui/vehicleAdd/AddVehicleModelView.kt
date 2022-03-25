package elektrogo.front.ui.vehicleAdd

import android.graphics.Bitmap
import android.text.Editable
import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.Vehicle
import kotlinx.coroutines.runBlocking

public class AddVehicleModelView : ViewModel() {
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

    fun sendVehicleInfo(vehicleInfo: Vehicle, bitmapVehiclePic: Bitmap): Int  = runBlocking{
        val statusCode  = FrontendController.sendVehicleInfo(vehicleInfo)
        FrontendController.sendVehiclePhoto(vehicleInfo.numberPlate, bitmapVehiclePic)
        return@runBlocking statusCode
    }


}