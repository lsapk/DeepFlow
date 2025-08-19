package com.lovable.dev.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JournalEntry(
    @SerialName("id")
    val id: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("title")
    val title: String,

    @SerialName("content")
    val content: String,

    @SerialName("mood")
    val mood: String? = null,

    // Supabase returns JSONB as a string, which we can then parse
    @SerialName("tags")
    val tags: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class DailyReflection(
    @SerialName("id")
    val id: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("question")
    val question: String,

    @SerialName("answer")
    val answer: String,

    @SerialName("created_at")
    val createdAt: String
)
