package elektrogo.front.ui.vehicleList

import androidx.lifecycle.ViewModel
import elektrogo.front.FrontendController
import kotlinx.coroutines.runBlocking

class VehicleListItemViewModel : ViewModel() {
    fun deleteVehicle(username: String, numberPlate: String) = runBlocking {
        FrontendController.deleteVehicle(username, numberPlate)
    }
}