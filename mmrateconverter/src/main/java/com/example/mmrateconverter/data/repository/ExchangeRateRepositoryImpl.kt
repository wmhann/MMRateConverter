package com.example.mmrateconverter.data.repository

import android.util.Log
import com.example.mmrateconverter.data.local.LocalDataSource
import com.example.mmrateconverter.data.local.TimestampDataSource
import com.example.mmrateconverter.data.mapper.toDomainEntity
import com.example.mmrateconverter.data.mapper.toRoomEntity
import com.example.mmrateconverter.data.remote.RemoteDataSource
import com.example.mmrateconverter.domain.entities.ExchangeRateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExchangeRateRepositoryImpl (
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val timestampDataSource: TimestampDataSource
) : ExchangeRateRepository {

    private val ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000L
    private val RATES_KEY = "rates_last_fetch"

    override fun getExchangeRates(): Flow<List<ExchangeRateEntity>> = flow {

        // 1. Local Cache ကို ချက်ချင်း ထုတ်ပေးခြင်း (Instant UI View)
        val cachedRates = localDataSource.getRatesList()
        if (cachedRates.isNotEmpty()) {
            emit(cachedRates.map { it.toDomainEntity() })
        }

        val lastFetchTime = timestampDataSource.getLastFetchTime(RATES_KEY)
        val isCacheOutdated = (System.currentTimeMillis() - lastFetchTime) > ONE_DAY_IN_MILLIS
        val isCacheEmpty = cachedRates.isEmpty()
        if (isCacheEmpty || isCacheOutdated) {
        // 2. Cloud Data ကို Fetch လုပ်ခြင်း (Network Check လုပ်ရန် လိုအပ်သည်)
        try {
            Log.d("REPO_SYNC", "Fetching Rates from server (Outdated/Empty)...")
            // remote fetch
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

            // C. Last Fetch Time ကို Update လုပ်ခြင်း (Key Point!)
            timestampDataSource.saveLastFetchTime(RATES_KEY, System.currentTimeMillis())

            // D. UI ကို Update လုပ်ရန် (နောက်ဆုံး Data ကို ပြန်ပို့သည်)
            emit(localDataSource.getRatesList().map { it.toDomainEntity() })

        } catch (e: Exception) {
            // ... အခြား Error များ
            Log.e("REPO_SYNC", "Remote fetch FAILED! Displaying local cache only. Error: ${e.message}", e)
            if (isCacheEmpty) throw e
        }
        } else {
            Log.d("REPO_SYNC", "Rates cache is fresh. No remote fetch needed.")
        }
    }

    override suspend fun toggleFavorite(rateId: String, isFavorite: Boolean) {
        // Favorite Status ကို Local DB မှာသာ ပြောင်းလဲသည်
        localDataSource.updateFavoriteStatus(rateId, isFavorite)
    }
}