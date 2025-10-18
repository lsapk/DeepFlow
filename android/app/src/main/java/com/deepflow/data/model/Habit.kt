package com.deepflow.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Habit(
    val id: String,
    val user_id: String,
    val title: String,
    val description: String?,
    val created_at: String,
    val updated_at: String?
)