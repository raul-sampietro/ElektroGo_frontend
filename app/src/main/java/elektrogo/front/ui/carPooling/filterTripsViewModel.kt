/**
 * @file filterTripsViewModel.java
 * @author Marina Alapont
 * @date 12/04/2022
 * @brief Implementacio d'un view model per tal de fer la comunicacio entre el fragment i el front controller.
 */
package elektrogo.front.ui.carPooling


import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import elektrogo.front.controller.FrontendController
import elektrogo.front.model.CarPooling
import elektrogo.front.model.RatingAvg
import kotlinx.coroutines.runBlocking

/**
 * @brief La clase filterTripsViewModel s'encarrega de la comunicacio de la GUI amb el front controller.
 */
class filterTripsViewModel : ViewModel() {
    /**
     * @brief Metode que es comunica amb FrontendController per tal d'obtenir tots les trajectes pels quals els valors coincideixen amb els parametres passats.
     * @param latLngOrigin latitud i longitud del origen del trajecte desitjat.
     * @param latLngDestination latitud i longitud del destí del trajecte desitjat.
     * @param dateIni data d'inici del trajecte
     * @param startTimeMin hora en la que es vol que com a mínim comenci el trajecte.
     * @param startTimeMax hora en la que es vol que com a macim comenci el trajecte.
     * @post Es retorna un llistat de objectes CarPooling que representen els trajectes que coincideixen amb la cerca.
     * @return Retorna un Pair < Int, Array<CarPooling>> on int es el status code i el array els trajectes resultants.
     */

   fun askForTrips(latLngOrigin: LatLng?, latLngDestination: LatLng?, dateIni:String?, startTimeMin:String?, startTimeMax:String?): Pair <Int, ArrayList<CarPooling>> = runBlocking{
        return@runBlocking FrontendController.getTrips(latLngOrigin!!.latitude, latLngOrigin.longitude, latLngDestination!!.latitude,latLngDestination.longitude,dateIni, startTimeMin, startTimeMax)
    }

    /**
     * @brief Metode que es comunica amb FrontendController per tal d'obtenir la valoracio mitjana d'un usuari.
     * @param username nom d'usuari del usuari per el que volem la valoracio mitjana.
     * @return Retorna un Pair<Int,RatingAvg> on el int es el code status i RatingAvg la valoracio mitjana de l'usuari i el nombre de valoracions que ha rebut.
     */

    fun getRating(username: String): Pair<Int, RatingAvg?> = runBlocking {
        return@runBlocking FrontendController.getRating(username)
    }

    /**
     * @brief Metode que es comunica amb FrontendController per tal d'obtenir el path o uri de la fotografia de perfil d'un usuari.
     * @param username nom d'usuari del usuari per el que volem la imatge de perfil.
     * @return Retorna un String que es el path de la imatge de perfil de l'usuari per el qual l'hem demanat.
     */
    fun getUsersProfilePhoto(username: String): String = runBlocking {
        return@runBlocking FrontendController.getUserProfilePhoto(username)
    }

    fun askForTripsDefault(): Pair<Int, ArrayList<CarPooling>> = runBlocking {
        return@runBlocking FrontendController.askForTripsDefault()
    }

}