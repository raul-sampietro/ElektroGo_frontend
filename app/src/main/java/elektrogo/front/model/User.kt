package elektrogo.front.model

import kotlinx.serialization.Serializable

@Serializable
data class User (
    var id : String,
    var provider : String,
    var username : String,
    var email : String,
    var name : String,
    var givenName : String,
    var familyName: String,
    var imageUrl : String
)