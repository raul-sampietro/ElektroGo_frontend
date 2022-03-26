package elektrogo.front.model

import kotlinx.serialization.Serializable

@Serializable
data class httpRespostes(
    val timestamp: String,
    val status: Int,
    val error: String,
    val path: String
)
