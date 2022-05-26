package elektrogo.front.model

import kotlinx.serialization.Serializable

data class Report(
    var userWhoReports: String,
    var reportedUser: String,
    var reason: String?
)