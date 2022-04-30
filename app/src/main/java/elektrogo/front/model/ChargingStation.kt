package elektrogo.front.model
import kotlinx.serialization.Serializable

@Serializable
data class ChargingStation(
    var id : Int,
    var promotor_gestor : String?,
    var acces : String?,
    var tipus_velocitat : String?,
    var tipus_connexio : String?,
    var latitude : Double,
    var longitude : Double,
    var designacio_descriptiva : String?,
    var kw : Double?,
    var ac_dc : String?,
    var ide_pdr : String?,
    var numberOfChargers : String?,
    var tipus_vehicle : String?
)
