
package com.example.mmrateconverter.data.remote

import android.util.Log
import com.example.mmrateconverter.data.local.entity.ExchangeRateRoomEntity
import com.example.mmrateconverter.data.remote.RemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await // Coroutines နဲ့ Firebase Task ကို တွဲသုံးဖို့
import com.example.mmrateconverter.data.remote.model.ExchangeRateRemoteModel
import com.example.mmrateconverter.data.remote.model.GoldPriceRemoteModel
import kotlinx.coroutines.flow.Flow


class FirebaseFirestoreDataSource(
    private val firestore: FirebaseFirestore // FirebaseFirestore Instance
) : RemoteDataSource {

    // Firestore Collection Name
    companion object {
        private const val RATE_COLLECTION = "exchangeRates"
        private const val GOLD_COLLECTION = "goldPrices"
        private const val TAG = "FirestoreDataSource"
    }

    override suspend fun fetchExchangeRates(): List<ExchangeRateRemoteModel> {
        try {
            val snapshot = firestore.collection(RATE_COLLECTION)
                .get()
                .await() // task ရဲ့ result ကို Suspend လုပ်ပြီး စောင့်ယူခြင်း

            if (snapshot.isEmpty) {
                Log.w(TAG, "Firestore fetch success but no documents found in '$RATE_COLLECTION' collection.")
                return emptyList()
            }

            Log.d(TAG, "Fetched ${snapshot.size()} documents from '$RATE_COLLECTION'.")

            val remoteModels = mutableListOf<ExchangeRateRemoteModel>()

            // Firestore Document များကို Loop ပတ်ပြီး Model အဖြစ် ပြောင်းလဲခြင်း
            for (document in snapshot.documents) {
                // Firestore ကနေ Data ကို Map အနေနဲ့ ဆွဲယူပြီး RateRemoteModel အဖြစ် ပြောင်းလဲခြင်း
                // Field Name တွေ မှန်ဖို့ လိုအပ်ပါတယ်။
                val model = ExchangeRateRemoteModel(
                    id = document.id, // Document ID ကို Currency Code အဖြစ် ယူခြင်း (ဥပမာ: "USD")
                    name = document.getString("name") ?: "",
                    rateToMMK = document.getDouble("rateToMMK") ?: 0.0,
                    lastUpdated = document.getTimestamp("lastUpdated")?.toDate()?.time
                        ?: System.currentTimeMillis()
                )
                remoteModels.add(model)
                Log.d(TAG, "Parsed document: ${document.id} -> $model")
            }
            return remoteModels
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching exchange rates from Firestore", e)
            // Re-throw the exception to be handled by the repository
            throw e
        }
    }

    // fetchGoldPrices() ကိုလည်း အလားတူ ရေးသားရပါမည်။
    override suspend fun fetchGoldPrices(): List<GoldPriceRemoteModel> {
       try {
            val snapshot = firestore.collection(GOLD_COLLECTION)
                .get()
                .await()

            if (snapshot.isEmpty) {
                Log.w(TAG, "Firestore fetch success but no documents found in '$GOLD_COLLECTION' collection.")
                return emptyList()
            }
             Log.d(TAG, "Fetched ${snapshot.size()} documents from '$GOLD_COLLECTION'.")

            val goldModels = mutableListOf<GoldPriceRemoteModel>()

            for (document in snapshot.documents) {
                val model = GoldPriceRemoteModel(
                    id = document.id, // e.g., "24K", "18K"
                    name = document.getString("type") ?: "",
                    price = document.getDouble("pricePerGram") ?: 0.0,
                    lastUpdated = document.getTimestamp("lastUpdated")?.toDate()?.time
                        ?: System.currentTimeMillis(),
                    unit = document.getString("unit") ?: ""
                )
                goldModels.add(model)
                 Log.d(TAG, "Parsed document: ${document.id} -> $model")
            }
            return goldModels
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching gold prices from Firestore", e)
            throw e
        }
    }
}
