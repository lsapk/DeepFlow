package com.deepflow.data.model

import kotlinx.serialization.Serializable

@Serializable
data class JournalEntry(
    val id: String,
    val user_id: String,
    val title: String,
    val content: String,
    val created_at: String,
    val updated_at: String?
)