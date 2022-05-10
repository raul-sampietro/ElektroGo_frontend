package elektrogo.front.ui.route

import com.google.android.gms.maps.model.LatLng
import elektrogo.front.controller.FrontendController
import kotlinx.coroutines.runBlocking
/**
 * @brief La classe RouteModelView te assignats els metodes que no depenen de la interficie de routeFragment.
 */
class RouteModelView {
    /**
     * @brief S'encarrega de fer les crides a FrontendController corresponents per tal d'enviar la informacio d'origen, desti i autonomia per la ruta i rebre els waypoints.
     * @param latLngDestination latitud i longitud del lloc de desti.
     * @param latLngOrigin latitud i longitud del lloc d'origen.
     * @param drivingRange autonomia del vehicle en aquell instant
     * @pre tots les parametres son no nulls
     * @return Retorna el statusCode dins d'una array en cas d'error o un array amb longituds i latituds que representen els waypoints en cas contrari.
     */
    fun sendRouteInfo(latLngOrigin: LatLng, latLngDestination:LatLng, drivingRange:Int): ArrayList<Double> = runBlocking {
          return@runBlocking FrontendController.sendRouteInfo(latLngOrigin.latitude, latLngOrigin.longitude, latLngDestination.latitude,latLngDestination.longitude,drivingRange)
    }

}