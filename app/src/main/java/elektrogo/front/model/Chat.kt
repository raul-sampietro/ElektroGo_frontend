package elektrogo.front.model
import kotlinx.serialization.Serializable

@Serializable
data class Chat (
    var userA : String,
    var userB : String,
    var messages : ArrayList<Message>
)