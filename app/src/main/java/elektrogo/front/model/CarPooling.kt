package elektrogo.front.model

import kotlinx.serialization.Serializable


@Serializable
data class CarPooling(
    var id: Long?,
    var startDate: String,
    var startTime: String,
    var offeredSeats: Int,
    var occupiedSeats: Int,
    var restrictions: String,
    var details: String,
    var latitudeOrigin: Double,
    var longitudeOrigin: Double,
    var originString: String,
    var latitudeDestination: Double,
    var longitudeDestination: Double,
    var destinationString: String,
    var vehicleNumberPlate: String,
    var username:String,
)