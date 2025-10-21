package app.deepflow.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.engine.android.Android

object SupabaseClient {
    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://xzgdfetnjnwrberyddmf.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inh6Z2RmZXRuanpud3JiZXJ5ZGRtZiIsIm9sZSI6ImFub24iLCJpYXQiOjE3MTk3NTI4MDMsImV4cCI6MjAzNTMyODgwM30.4m24F88b-aCe-vax-p_jf_o61M58II5_1so-9-1z_bE"
    ) {
        install(Postgrest)
        engine = Android.create()
    }
}
