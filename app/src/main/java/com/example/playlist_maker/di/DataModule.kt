package com.example.playlist_maker.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.playlist_maker.data.db.TrackDatabase
import com.example.playlist_maker.data.search.NetworkClient
import com.example.playlist_maker.data.search.network.RetrofitNetworkClient
import com.example.playlist_maker.data.search.network.TrackApi
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    factory { Gson() }

    factory<SharedPreferences> { (name: String) ->
        androidContext().getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    single<TrackApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackApi::class.java)
    }

    single {
        Room.databaseBuilder(androidContext(), TrackDatabase::class.java, "track_favorite_database")
            .build()
    }
}