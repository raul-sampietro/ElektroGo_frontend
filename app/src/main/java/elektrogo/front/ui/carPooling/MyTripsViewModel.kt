package elektrogo.front.ui.CarPooling

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.CarPooling
import kotlinx.coroutines.runBlocking

class MyTripsViewModel : ViewModel() {

    fun askForTrips(latLngOrigin: LatLng?, latLngDestination: LatLng?, dateIni:String?, startTimeMin:String?, startTimeMax:String?): Pair <Int, ArrayList<CarPooling>> = runBlocking{
        return@runBlocking FrontendController.getTrips(latLngOrigin!!.latitude, latLngOrigin.longitude, latLngDestination!!.latitude,latLngDestination.longitude,dateIni, startTimeMin, startTimeMax)
    }
    // TODO: Implement the ViewModel
}