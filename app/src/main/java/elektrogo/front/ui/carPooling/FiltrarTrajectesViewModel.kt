package elektrogo.front.ui.carPooling

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import elektrogo.front.controller.FrontendController
import kotlinx.coroutines.runBlocking

class FiltrarTrajectesViewModel : ViewModel() {
    fun askForTrips(latLngOrigin: LatLng?, latLngDestination: LatLng?, dateIni:String?, startTimeMin:String?, startTimeMax:String?): ArrayList<CarPooling> = runBlocking{
        return@runBlocking FrontendController.getTrips(latLngOrigin.latitude, latLngOrigin.longitude, latLngDestination.latitude,latLngDestination.longitude,dateIni, startTimeMin, startTimeMax)
    }
}