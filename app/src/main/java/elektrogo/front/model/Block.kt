package elektrogo.front.model

import kotlinx.serialization.Serializable

@Serializable
class Block (
    var userBlocking : String,
    var blockUser : String
)