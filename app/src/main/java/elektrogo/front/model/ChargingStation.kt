package elektrogo.front.model
import kotlinx.serialization.Serializable

@Serializable
data class ChargingStation(
    var id : Int,
    var latitude : Double,
    var longitude : Double,
    var numberOfChargers : Int
)
