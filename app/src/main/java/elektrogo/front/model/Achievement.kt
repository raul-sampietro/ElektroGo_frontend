package elektrogo.front.model

import kotlinx.serialization.Serializable

@Serializable
data class Achievement (
    var username : String,
    var achievement : String,
    var points : Int
)
