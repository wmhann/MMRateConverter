package com.example.mmrateconverter.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.mmrateconverter.data.local.entity.GoldRoomEntity

@Dao
interface GoldDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoldPrices(prices: List<GoldRoomEntity>)

    @Query("SELECT * FROM gold_prices_table")
    fun getAllGoldPrices(): Flow<List<GoldRoomEntity>>

    @Query("SELECT * FROM gold_prices_table")
    suspend fun getGoldPricesAsList(): List<GoldRoomEntity>
    @Query("UPDATE gold_prices_table SET isFavorite = :favorite WHERE id = :goldId")
    abstract fun updateGoldFavoriteStatus(goldId: String, favorite: Boolean)
}
