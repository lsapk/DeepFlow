package com.deepflow.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Goal(
    val id: String,
    val user_id: String,
    val title: String,
    val description: String?,
    val created_at: String,
    val updated_at: String?,
    val is_completed: Boolean
)