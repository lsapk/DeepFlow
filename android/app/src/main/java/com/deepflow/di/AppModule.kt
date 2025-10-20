package com.deepflow.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://xzgdfetnjnwrberyddmf.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inh6Z2RmZXRuam53cmJlcnlkZG1mIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDIzMjk4MTksImV4cCI6MjA1NzkwNTgxOX0.XJFYvBiZHo1vcfCVFf79C9U6LP4Vuf05PCixBWqaYU"
        ) {
            install(io.github.jan.supabase.gotrue.GoTrue)
            install(io.github.jan.supabase.postgrest.Postgrest)
        }
    }
}