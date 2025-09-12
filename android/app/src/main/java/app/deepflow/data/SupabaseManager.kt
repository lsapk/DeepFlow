package app.deepflow.data

import io.supabase.SupabaseClient
import io.supabase.gotrue.GoTrue
import io.supabase.postgrest.Postgrest
import io.supabase.realtime.Realtime

object SupabaseManager {

    private const val SUPABASE_URL = "https://xzgdfetnjnwrberyddmf.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inh6Z2RmZXRuam53cmJlcnlkZG1mIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDIzMjk4MTksImV4cCI6MjA1NzkwNTgxOX0.XJFYvBiZHo1vcfCV6Fn79C9U6LP4Vuf05PCixBWqaYU"

    val client = SupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(GoTrue)
        install(Postgrest)
        install(Realtime)
    }
}
