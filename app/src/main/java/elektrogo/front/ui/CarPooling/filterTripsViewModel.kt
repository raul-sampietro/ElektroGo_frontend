/**
 * @file filterTripsViewModel.java
 * @author Marina Alapont
 * @date 12/04/2022
 * @brief Implementacio d'un view model per tal de fer la comunicació entre el fragment i el front controller.
 */
package elektrogo.front.ui.carPooling

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.CarPooling
import kotlinx.coroutines.runBlocking

/**
 * @brief La clase filterTripsViewModel s'encarrega de la comunicació de la GUI amb el front controller.
 */
class filterTripsViewModel : ViewModel() {

   fun askForTrips(latLngOrigin: LatLng?, latLngDestination: LatLng?, dateIni:String?, startTimeMin:String?, startTimeMax:String?): Pair <Int, ArrayList<CarPooling>> = runBlocking{
        return@runBlocking FrontendController.getTrips(latLngOrigin!!.latitude, latLngOrigin.longitude, latLngDestination!!.latitude,latLngDestination.longitude,dateIni, startTimeMin, startTimeMax)
    }

    fun getRating(username: String): Pair<Int, Double> = runBlocking {
        return@runBlocking FrontendController.getRating(username)
    }

    fun getUsersProfilePhoto(username: String): String = runBlocking {
        return@runBlocking FrontendController.getUserProfilePhoto(username)
    }

}