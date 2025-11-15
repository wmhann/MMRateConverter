package com.example.mmrateconverter.data.repository

import android.util.Log
import com.example.mmrateconverter.data.local.LocalDataSource
import com.example.mmrateconverter.data.mapper.toDomainEntity
import com.example.mmrateconverter.data.mapper.toRoomEntity
import com.example.mmrateconverter.data.remote.RemoteDataSource
import com.example.mmrateconverter.domain.entities.ExchangeRateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class ExchangeRateRepositoryImpl (
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : ExchangeRateRepository {

    override fun getExchangeRates(): Flow<List<ExchangeRateEntity>> = flow {

        // 1. Local Cache ကို ချက်ချင်း ထုတ်ပေးခြင်း (Instant UI View)
        val cachedRates = localDataSource.getRatesList()
        if (cachedRates.isNotEmpty()) {
            emit(cachedRates.map { it.toDomainEntity() })
        }

        // 2. Cloud Data ကို Fetch လုပ်ခြင်း (Network Check လုပ်ရန် လိုအပ်သည်)
        try {
            Log.d("REPO_SYNC", "Attempting to fetch remote rates...")
            val remoteRates = remoteDataSource.fetchExchangeRates()
            // 3. Mapper Logic: Remote က Data ကို Local မှာ သိမ်းမယ့်အခါ Favorite Status ကို Local ကနေ ပြန်ယူရပါမယ်
            val currentLocalRates = localDataSource.getRatesList() // လက်ရှိ Local Rates ကို ယူ

            val ratesToSave = remoteRates.map { remoteModel ->
                // Remote ကလာတဲ့ Rate ကို Local ရှိ Favorite Status နဲ့ ပေါင်းပြီး Room Entity အဖြစ် ပြောင်းသည်
                val isFav = currentLocalRates.find { it.id == remoteModel.id }?.isFavorite ?: false
                remoteModel.toRoomEntity(isFavorite = isFav)
            }
            emit(localDataSource.getRatesList().map { it.toDomainEntity() })


            // 4. Local Cache ကို Update လုပ်ခြင်း
            localDataSource.saveRates(ratesToSave)

            // Local DB ကို Update လုပ်လိုက်တာနဲ့၊ အဆင့် (1) မှာ သုံးထားတဲ့ getRatesFlow() က
            // အလိုအလျောက် New Data ကို ထပ်ထုတ်ပေးပါလိမ့်မယ် (Flow ရဲ့ သဘောသဘာဝ)

        } catch (e: IOException) {
            // 5. Network Error (သို့) Firebase Error ကို ကိုင်တွယ်ခြင်း
            // အင်တာနက် မရှိရင် ဒီနေရာကို ရောက်ပါမည်။ Local Cache ကိုပဲ ဆက်ပြပါမည်။
            // Log.e("Repository", "Failed to fetch remote data. Displaying local cache.", e)
            Log.e("REPO_SYNC", "Remote fetch FAILED! Displaying local cache only.", e)

        } catch (e: Exception) {
            // ... အခြား Error များ
            Log.e("REPO_SYNC", "Remote fetch FAILED! Displaying local cache only. Error: ${e.message}", e)
        }
    }

    override suspend fun toggleFavorite(rateId: String, isFavorite: Boolean) {
        // Favorite Status ကို Local DB မှာသာ ပြောင်းလဲသည်
        localDataSource.updateFavoriteStatus(rateId, isFavorite)
    }
}