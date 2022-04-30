/**
 * @file AllChargingStations.kt
 * @author Simon Helmuth Oliva Stark
 * @brief Aquesta es un objecte singleton que conte totes les estacions de carrega presents a la base de dades de backend.
 */

package elektrogo.front.model

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import elektrogo.front.R
import elektrogo.front.controller.FrontendController
import kotlinx.coroutines.runBlocking

/**
 * @brief L'objecte AllChargingStations es un singleton que emmagatzema totes les ChargingStations del sistema.
 */
object AllChargingStations {

    /**
     * @brief Status de l'ultima crida HTTP per obtenir les estacions de la BD de backend.
     */
    private var lastStatus:Int = 0

    /**
     * @brief HashMap que conte totes les ChargingStations, usant com a clau el seu id.
     */
    private var allStations = initialize()

    /**
     * @brief Metode que inicialitza allStations.
     * @pre
     * @post Si s'ha pogut connectar amb el servidor, totes les ChargingStations de la BD s'han afegit a allStations, i tenen com a clau el seu id.
     */
    private fun initialize(): HashMap<Int, ChargingStation> {
        try {
            val statusAndStations = runBlocking { FrontendController.getChargingPoints() }

            lastStatus = statusAndStations.first

            val stationsMap = hashMapOf<Int, ChargingStation>()

            /*//prueba
            var cs = ChargingStation(-1, null,null, null,null,41.464458, 2.196703, null, null, null,null, null, null)
            stationsMap[cs.id] = cs
            //prueba*/

            if (lastStatus == 200) {
                val stationsArray = statusAndStations.second
                for (stat in stationsArray) stationsMap[stat.id] = stat
            }
            return stationsMap
        }
        catch (e: Exception) {
            lastStatus = -1
            return hashMapOf<Int, ChargingStation>()
        }
    }

    /**
     * @brief Metode per obtenir el status de l'ultima crida HTTP per obtenir les estacions de la BD de backend.
     * @pre
     * @post Retorna lastStatus
     */
    fun getLastStatus():Int {
        return lastStatus
    }

    /**
     * @brief Metode per obtenir una ChargingStation per id.
     * @param id identificador de la CharginStations que es vol obtenir.
     * @pre
     * @post Retorna la ChargingStation amb l'id indicat si existeix, si no retorna null.
     */
    fun getStation(id: Int):ChargingStation? {
        return if (allStations.containsKey(id)) {
            allStations[id]
        } else null
    }

    /**
     * @brief Metode per obtenir totes les estacions de carrega guardades al sistema.
     * @pre
     * @post Retorna allStations
     */
    fun getAllStations(): HashMap<Int, ChargingStation> {
        return allStations
    }

}