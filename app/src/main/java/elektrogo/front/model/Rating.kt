package elektrogo.front.model
import kotlinx.serialization.Serializable

@Serializable
data class Rating(
    var userWhoRates: String,
    var ratedUser: String,
    var points: Int,
    var comment: String?
)
