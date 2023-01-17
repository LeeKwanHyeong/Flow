package com.example.flow.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [MovieModel::class],
    version = 1
)
abstract class MovieDb: RoomDatabase() {
    abstract fun movieDao(): MovieDao
}