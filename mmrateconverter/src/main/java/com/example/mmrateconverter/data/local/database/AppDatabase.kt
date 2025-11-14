package com.example.mmrateconverter.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mmrateconverter.data.local.dao.GoldDao
import com.example.mmrateconverter.data.local.dao.RateDao
import com.example.mmrateconverter.data.local.entity.ExchangeRateRoomEntity
import com.example.mmrateconverter.data.local.entity.GoldRoomEntity

@Database(
    entities = [ExchangeRateRoomEntity::class, GoldRoomEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rateDao(): RateDao
    abstract fun goldDao(): GoldDao
}