package com.lovable.dev.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    @SerialName("id")
    val id: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String? = null,

    @SerialName("completed")
    val completed: Boolean = false,

    @SerialName("due_date")
    val dueDate: String? = null,

    @SerialName("priority")
    val priority: String? = null,

    @SerialName("linked_goal_id")
    val linkedGoalId: String? = null,

    @SerialName("parent_task_id")
    val parentTaskId: String? = null,

    @SerialName("sort_order")
    val sortOrder: Int? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class Subtask(
    @SerialName("id")
    val id: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("parent_task_id")
    val parentTaskId: String,

    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String? = null,

    @SerialName("completed")
    val completed: Boolean = false,

    @SerialName("priority")
    val priority: String? = null,

    @SerialName("sort_order")
    val sortOrder: Int? = null,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String
)
