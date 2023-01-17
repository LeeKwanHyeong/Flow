package com.example.flow.data

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: MovieModel)

    @Update
    fun update(data: MovieModel)

    suspend fun upsert(data: MovieModel){
        Log.e("HelloWorld", "HellOWorld")
        try {
            insert(data)
        }catch (e: SQLiteConstraintException){
            update(data)
        }catch (e: IllegalStateException){
            update(data)
        }catch (e: SQLiteException){
            update(data)
        }
    }

    @Query(
        "SELECT * FROM MovieModel limit 10"
    )
    fun getMovies(): List<MovieModel>

}