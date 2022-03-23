package elektrogo.front.ui.vehicleList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import elektrogo.front.FrontendController
import elektrogo.front.Vehicle
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class VehicleListViewModel : ViewModel() {
    private lateinit var string: String

    fun getVehicleList(username: String) : ArrayList<Vehicle> = runBlocking {
        FrontendController.getVehicleList(username)
    }
}