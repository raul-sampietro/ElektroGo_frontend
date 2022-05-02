package elektrogo.front.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var username : String,
    var email : String,
    var id : String,
    var provider : String,
    var name : String,
    var givenName : String,
    var familyName : String,
    var imageUrl : String
)