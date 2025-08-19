package com.lovable.dev.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    @SerialName("id")
    val id: String,

    @SerialName("display_name")
    val displayName: String? = null,

    @SerialName("email")
    val email: String? = null,

    @SerialName("bio")
    val bio: String? = null,

    @SerialName("photo_url")
    val photoUrl: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class UserSettings(
    @SerialName("id")
    val id: String,

    @SerialName("dark_mode")
    val darkMode: Boolean? = null,

    @SerialName("notifications_enabled")
    val notificationsEnabled: Boolean? = null,

    @SerialName("language")
    val language: String? = null,

    @SerialName("theme")
    val theme: String? = null,

    @SerialName("karma_points")
    val karmaPoints: Int? = null,

    @SerialName("unlocked_features")
    val unlockedFeatures: String? = null, // JSONB as String

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null
)
