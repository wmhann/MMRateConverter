package com.example.mmrateconverter.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.example.mmrateconverter.data.local.entity.ExchangeRateRoomEntity

@Dao
interface RateDao {
    // Data အသစ်တွေကို ထည့်သွင်းခြင်း (တူရင် အစားထိုးပါမည်)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRates(rates: List<ExchangeRateRoomEntity>)

    // Rate အားလုံးကို Flow အနေနဲ့ ရယူခြင်း (Real-time DB update အတွက်)
    @Query("SELECT * FROM exchange_rates_table")
    fun getAllRates(): Flow<List<ExchangeRateRoomEntity>>

    // Favorite အခြေအနေကို ပြောင်းလဲခြင်း (id ကို အခြေခံ၍)
    @Query("UPDATE exchange_rates_table SET isFavorite = :isFavorite WHERE id = :rateId")
    suspend fun updateFavoriteStatus(rateId: String, isFavorite: Boolean)

    // Rate များကို List အနေဖြင့် တစ်ခါတည်း ရယူခြင်း (Initial Sync အတွက်)
    @Query("SELECT * FROM exchange_rates_table")
    suspend fun getRatesAsList(): List<ExchangeRateRoomEntity>
}