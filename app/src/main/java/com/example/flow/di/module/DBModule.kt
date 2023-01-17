package com.example.flow.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.flow.data.MovieDao
import com.example.flow.data.MovieDb
import com.example.flow.di.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DBModule {
    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(application: Application): Context {
        return application
    }

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext app: Context): MovieDb {
        return Room
            .databaseBuilder(app, MovieDb::class.java, "movie.db")
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(db: MovieDb): MovieDao{
        return db.movieDao()
    }
}