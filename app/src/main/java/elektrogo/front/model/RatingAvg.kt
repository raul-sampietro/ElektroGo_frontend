package elektrogo.front.model

import kotlinx.serialization.Serializable

@Serializable
data class RatingAvg(
    var ratingValue : Double,
    var numberOfRatings : Int
)
