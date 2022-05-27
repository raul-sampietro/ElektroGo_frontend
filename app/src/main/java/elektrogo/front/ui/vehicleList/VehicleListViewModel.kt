package elektrogo.front.ui.vehicleList

import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import elektrogo.front.controller.session.SessionController
import elektrogo.front.model.Driver
import elektrogo.front.model.Vehicle
import kotlinx.coroutines.runBlocking

class VehicleListViewModel : ViewModel() {
    private lateinit var string: String

    fun getVehicleList(username: String) : ArrayList<Vehicle> = runBlocking {
        FrontendController.getVehicleList(username)
    }

    fun getDriver(username: String) : Pair<Int, Driver?> = runBlocking {
        FrontendController.getDriver2(username)
    }
}