package com.deepflow.data.repository

import com.deepflow.data.model.Goal
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject

class GoalRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {
    suspend fun getGoals(): List<Goal> {
        return supabaseClient.postgrest["goals"].select().decodeList<Goal>()
    }

    suspend fun createGoal(title: String, description: String) {
        val goal = Goal(id = "", user_id = "", title = title, description = description, created_at = "", updated_at = null, is_completed = false)
        supabaseClient.postgrest["goals"].insert(goal)
    }

    suspend fun updateGoal(id: String, title: String, description: String, isCompleted: Boolean) {
        supabaseClient.postgrest["goals"].update({
            set("title", title)
            set("description", description)
            set("is_completed", isCompleted)
        }) {
            filter { eq("id", id) }
        }
    }

    suspend fun deleteGoal(id: String) {
        supabaseClient.postgrest["goals"].delete {
            filter { eq("id", id) }
        }
    }
}