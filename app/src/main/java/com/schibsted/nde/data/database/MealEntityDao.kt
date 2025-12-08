package com.schibsted.nde.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealEntityDao {
    @Query("SELECT * FROM meal ORDER BY id")
    suspend fun getAll(): List<MealEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(meals: List<MealEntity>)
}
