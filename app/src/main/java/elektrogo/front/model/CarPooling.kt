/**
 * @file CarPooling.kt
 * @author Joel Cardona
 * @date 03/05/2022
 * @brief Classe serialitzable de CarPooling
 */
package elektrogo.front.model

import kotlinx.serialization.Serializable
import java.sql.Date
import java.sql.Time
import java.time.LocalDate

@Serializable
data class CarPooling(
    /**
     * @brief id del CarPooling
     */
    var id: Long?,
    /**
     * @brief data d'inici del trajecte
     */
    var startDate: String,
    /**
     * @brief hora d'inici del trajecte
     */
    var startTime: String,
    /**
     * @brief seients ofresos en el trajecte
     */
    var offeredSeats: Int,
    /**
     * @brief seients ocupats del trajecte
     */
    var occupiedSeats: Int,
    /**
     * @brief restriccions del trajecte
     */
    var restrictions: String?,
    /**
     * @brief detalls del trajecte
     */
    var details: String?,
    /**
     * @brief matricula del vehicle del trajecte
     */
    var vehicleNumberPlate: String,
    /**
     * @brief nom de la direccio del origin
     */
    var origin: String,
    /**
     * @brief nom de la direccio de destinacio
     */
    var destination: String,
    /**
     * @brief nom de l'usuari creador del trajecte
     */
    var username: String,
    /**
     * @brief data limit de cancelacio d'un trajecte
     */
    var cancelDate: String,
    /**
     * @brief es l'estat en que es troba el trajecte
     */
    var state: String,
    /**
     * @brief coordenada latitud de l'origen
     */
    var latitudeOrigin: Double,
    /**
     * @brief coordenada longitud de l'origen
     */
    var longitudeOrigin: Double,
    /**
     * @brief coordenada latitud del desti
     */
    var latitudeDestination: Double,
    /**
     * @brief coordenada longitud del desti
     */
    var longitudeDestination: Double,
)
