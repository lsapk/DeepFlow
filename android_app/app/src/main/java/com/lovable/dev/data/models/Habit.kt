package com.lovable.dev.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Habit(
    @SerialName("id")
    val id: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String? = null,

    @SerialName("category")
    val category: String? = null,

    @SerialName("frequency")
    val frequency: String,

    @SerialName("target")
    val target: Int,

    @SerialName("streak")
    val streak: Int? = null,

    @SerialName("last_completed_at")
    val lastCompletedAt: String? = null,

    @SerialName("is_archived")
    val isArchived: Boolean? = null,

    @SerialName("linked_goal_id")
    val linkedGoalId: String? = null,

    @SerialName("sort_order")
    val sortOrder: Int? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class HabitCompletion(
    @SerialName("id")
    val id: String,

    @SerialName("user_id")
    val userId: String?,

    @SerialName("habit_id")
    val habitId: String?,

    @SerialName("completed_date")
    val completedDate: String?,

    @SerialName("created_at")
    val createdAt: String? = null
)
