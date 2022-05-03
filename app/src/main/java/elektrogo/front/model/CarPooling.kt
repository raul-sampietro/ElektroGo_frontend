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
    var offeredSeats: Int,
    var occupiedSeats: Int,
    var restrictions: String?,
    var details: String?,
    var vehicleNumberPlate: String,
    var origin: String,
    var destination: String,
    var username: String,
    var cancelDate: String,
    var latitudeOrigin: Double,
    var longitudeOrigin: Double,
    var latitudeDestination: Double,
    var longitudeDestination: Double,
)
