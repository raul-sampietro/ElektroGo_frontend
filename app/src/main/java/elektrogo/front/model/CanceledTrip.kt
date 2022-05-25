package elektrogo.front.model
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CanceledTrip(
    var id: Long,
    var dayCanceled: String,
    var reason: String?
)
