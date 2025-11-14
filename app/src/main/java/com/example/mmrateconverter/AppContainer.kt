package com.example.mmrateconverter

import com.example.mmrateconverter.data.remote.FirebaseFirestoreDataSource
import com.example.mmrateconverter.data.remote.RemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore // Firebase ကို စတင်အသုံးပြုရန်
import com.google.firebase.Firebase

/**
 * Dependency Injection အတွက် Manually ဖန်တီးထားသော Container
 * လိုအပ်သော Data Source များ၊ Repository များ၏ Instance များကို ဖန်တီးပေးသည်။
 */
interface AppContainer {
    val remoteDataSource: RemoteDataSource
    // အခြား Repository များ သို့မဟုတ် LocalDataSource များ ဒီနေရာတွင် ရှိပါမည်
}

class DefaultAppContainer : AppContainer {

    // 1. Firebase Firestore Instance ကို ရယူခြင်း
    private val firebaseFirestoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    // 2. Remote Data Source (Firestore) ကို ဖန်တီးခြင်း
    // FirebaseFirestoreDataSource ကို firestore instance ထည့်ပြီး Constructor ခေါ်ခြင်း
    override val remoteDataSource: RemoteDataSource by lazy {
        FirebaseFirestoreDataSource(firestore = firebaseFirestoreInstance)
    }

    // 3. Repository တွေကလည်း ဒီနေရာမှာ ဖန်တီးရမှာပါ
    // ဥပမာ:
    // override val exchangeRateRepository: ExchangeRateRepository by lazy {
    //      ExchangeRateRepositoryImpl(remoteDataSource)
    // }
}