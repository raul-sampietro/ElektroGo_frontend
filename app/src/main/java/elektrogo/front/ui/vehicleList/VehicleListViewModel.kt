package elektrogo.front.ui.vehicleList

import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.Vehicle
import kotlinx.coroutines.runBlocking

class VehicleListViewModel : ViewModel() {
    private lateinit var string: String

    fun getVehicleList(username: String) : ArrayList<Vehicle> = runBlocking {
        FrontendController.getVehicleList(username)
    }
}