package elektrogo.front.ui.CarPooling

import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.Vehicle
import kotlinx.coroutines.runBlocking

class NewCarPoolingFragmentViewModel : ViewModel() {
    fun getVehicleList(username: String) : ArrayList<Vehicle> = runBlocking {
        FrontendController.getVehicleList(username)
    }
}