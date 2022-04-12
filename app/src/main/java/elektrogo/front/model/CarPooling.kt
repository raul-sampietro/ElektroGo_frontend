package elektrogo.front.model

import kotlinx.serialization.Serializable
import java.sql.Date
import java.sql.Time
import java.time.LocalDate

@Serializable
data class CarPooling(
    var id: Long?,
    var startDate: String,
    var startTime: String,
    var offeredSears: Int,
    var occupiedSeats: Int,
    var restrictions: String,
    var details: String,
    var latitudeOrigin: Double,
    var longitudeOrigin: Double,
    var originString: String,
    var latitudeDestination: Double,
    var longitudeDestination: Double,
    var destinationString: Double,
    var vehicleNumberPlate: String,
)