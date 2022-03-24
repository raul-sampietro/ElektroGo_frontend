package elektrogo.front.ui

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class VehicleModel(val brand:String, val model:String, val numberPlate: String, val drivingRange: Int, val fabricationYear:Int, val seats: Int, val imageId: String?)

