package com.deepflow.data.repository

import com.deepflow.data.model.Habit
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject

class HabitRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {
    suspend fun getHabits(): List<Habit> {
        return supabaseClient.postgrest["habits"].select().decodeList<Habit>()
    }

    suspend fun createHabit(title: String, description: String) {
        val habit = Habit(id = "", user_id = "", title = title, description = description, created_at = "", updated_at = null)
        supabaseClient.postgrest["habits"].insert(habit)
    }

    suspend fun updateHabit(id: String, title: String, description: String) {
        supabaseClient.postgrest["habits"].update({
            set("title", title)
            set("description", description)
        }) {
            filter { eq("id", id) }
        }
    }

    suspend fun deleteHabit(id: String) {
        supabaseClient.postgrest["habits"].delete {
            filter { eq("id", id) }
        }
    }
}