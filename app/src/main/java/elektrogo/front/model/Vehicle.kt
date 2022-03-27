package elektrogo.front.model

import kotlinx.serialization.Serializable

@Serializable
data class Vehicle(
    var brand : String,
    var model : String,
    var numberPlate : String,
    var drivingRange : Int,
    var fabricationYear : Int,
    var seats : Int,
    var imageId : String?
)
