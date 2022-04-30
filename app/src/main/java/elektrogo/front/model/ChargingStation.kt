package elektrogo.front.model
import kotlinx.serialization.Serializable

@Serializable
data class ChargingStation(
    var id : Int,
    var promotorGestor : String,
    var acces : String,
    var tipusVelocitat : String,
    var tipusConnexio : String,
    var latitude : Double,
    var longitude : Double,
    var descriptiva_deseignacio : String,
    var kw : Double,
    var AcDc : String,
    var ident : String,
    var numeroPlaces : String,
    var tipus_vehicle : String
)
