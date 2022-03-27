package elektrogo.front.ui.Route

import com.google.android.gms.maps.model.LatLng
import elektrogo.front.controller.FrontendController
import kotlinx.coroutines.runBlocking

class RouteModelView {

    fun sendRouteInfo(latLngOrigin: LatLng, latLngDestination:LatLng, drivingRange:Int): ArrayList<Double> = runBlocking {
          return@runBlocking FrontendController.sendRouteInfo(latLngOrigin.latitude, latLngOrigin.longitude, latLngDestination.latitude,latLngDestination.longitude,drivingRange)
    }

}