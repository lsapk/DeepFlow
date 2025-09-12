package app.deepflow.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ... (Other models remain the same)

@Serializable
data class FocusSession(
    val id: String,
    val title: String,
    val duration: Int, // in minutes
    @SerialName("user_id")
    val userId: String,
    @SerialName("started_at")
    val startedAt: String,
    @SerialName("completed_at")
    val completedAt: String? = null,
    @SerialName("created_at")
    val createdAt: String
)
