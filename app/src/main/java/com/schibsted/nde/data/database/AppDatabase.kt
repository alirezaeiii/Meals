package com.schibsted.nde.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MealEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mealDao(): MealEntityDao
}