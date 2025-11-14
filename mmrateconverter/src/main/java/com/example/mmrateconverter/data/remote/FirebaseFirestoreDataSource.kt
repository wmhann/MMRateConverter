import com.example.mmrateconverter.data.local.entity.ExchangeRateRoomEntity
import com.example.mmrateconverter.data.remote.RemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await // Coroutines နဲ့ Firebase Task ကို တွဲသုံးဖို့
import com.example.mmrateconverter.data.remote.model.ExchangeRateRemoteModel
import com.example.mmrateconverter.data.remote.model.GoldPriceRemoteModel
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    fun getExchangeRates(): Flow<List<ExchangeRateRoomEntity>>
    suspend fun updateFavorite(rateId: String, isFavorite: Boolean)
}
class FirebaseFirestoreDataSource(
    private val firestore: FirebaseFirestore // FirebaseFirestore Instance
) : RemoteDataSource {

    // Firestore Collection Name
    companion object {
        private const val RATE_COLLECTION = "exchangeRates"
        private const val GOLD_COLLECTION = "goldPrices"
    }

    override suspend fun fetchExchangeRates(): List<ExchangeRateRemoteModel> {
        // Coroutines နဲ့ Firebase Task ကို await သုံးပြီး Suspend လုပ်ထားခြင်း
        val snapshot = firestore.collection(RATE_COLLECTION)
            .get()
            .await() // task ရဲ့ result ကို Suspend လုပ်ပြီး စောင့်ယူခြင်း

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
        }
        return remoteModels
    }

    // fetchGoldPrices() ကိုလည်း အလားတူ ရေးသားရပါမည်။
    override suspend fun fetchGoldPrices(): List<GoldPriceRemoteModel> {
        val snapshot = firestore.collection(GOLD_COLLECTION)
            .get()
            .await()

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
        }
        return goldModels
    }
}