package elektrogo.front.model
import kotlinx.serialization.Serializable

@Serializable
data class Message (
    var sender : String,
    var receiver: String,
    var message : String,
    var sentAt: String, //parse in the model
)