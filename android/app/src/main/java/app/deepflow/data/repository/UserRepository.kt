package app.deepflow.data.repository

import app.deepflow.data.SupabaseClient
import io.github.jan.supabase.gotrue.auth

class UserRepository {

    suspend fun signIn(email: String, password: String) {
        SupabaseClient.client.auth.signInWith(email, password)
    }
}
