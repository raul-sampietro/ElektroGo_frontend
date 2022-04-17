package elektrogo.front.model
import kotlinx.serialization.Serializable

@Serializable
data class Message (
    var sender : String,
    var receiver: String,
    var text : String,
    var timestamp: String, //parse in the model
)