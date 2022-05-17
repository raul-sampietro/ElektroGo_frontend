package elektrogo.front.ui.report

import androidx.lifecycle.ViewModel
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.Vehicle
import kotlinx.coroutines.runBlocking

class ReportUserModelView : ViewModel() {
    fun sendVehicleInfo(vehicleInfo: Vehicle, username: String): Int  = runBlocking{
        return@runBlocking  FrontendController.sendVehicleInfo(vehicleInfo, username)
    }
}