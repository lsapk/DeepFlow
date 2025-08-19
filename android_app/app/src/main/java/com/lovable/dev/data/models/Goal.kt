package com.lovable.dev.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Goal(
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

    @SerialName("completed")
    val completed: Boolean = false,

    @SerialName("progress")
    val progress: Int? = null,

    @SerialName("target_date")
    val targetDate: String? = null,

    @SerialName("is_archived")
    val isArchived: Boolean? = null,

    @SerialName("sort_order")
    val sortOrder: Int? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class Subobjective(
    @SerialName("id")
    val id: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("parent_goal_id")
    val parentGoalId: String,

    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String? = null,

    @SerialName("completed")
    val completed: Boolean = false,

    @SerialName("sort_order")
    val sortOrder: Int? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null
)
