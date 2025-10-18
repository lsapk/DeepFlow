package com.deepflow.data.repository

import com.deepflow.data.model.JournalEntry
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject

class JournalRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {
    suspend fun getJournalEntries(): List<JournalEntry> {
        return supabaseClient.postgrest["journal_entries"].select().decodeList<JournalEntry>()
    }

    suspend fun createJournalEntry(title: String, content: String) {
        val entry = JournalEntry(id = "", user_id = "", title = title, content = content, created_at = "", updated_at = null)
        supabaseClient.postgrest["journal_entries"].insert(entry)
    }

    suspend fun updateJournalEntry(id: String, title: String, content: String) {
        supabaseClient.postgrest["journal_entries"].update({
            set("title", title)
            set("content", content)
        }) {
            filter { eq("id", id) }
        }
    }

    suspend fun deleteJournalEntry(id: String) {
        supabaseClient.postgrest["journal_entries"].delete {
            filter { eq("id", id) }
        }
    }
}