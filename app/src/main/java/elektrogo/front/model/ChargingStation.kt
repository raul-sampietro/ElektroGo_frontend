package elektrogo.front.model
import kotlinx.serialization.Serializable

@Serializable
data class ChargingStation(
    var id : Int,
    var latitude : Double, //són BigDecimal a backend, però no es podien serialitzar
    var longitude : Double,
    var numberOfChargers : Int
)
