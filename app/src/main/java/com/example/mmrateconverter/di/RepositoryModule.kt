package com.example.mmrateconverter.di

import com.example.mmrateconverter.data.local.LocalDataSource
import com.example.mmrateconverter.data.local.LocalDataSourceImpl
import com.example.mmrateconverter.data.local.dao.GoldDao
import com.example.mmrateconverter.data.local.dao.RateDao
import com.example.mmrateconverter.data.remote.FirebaseFirestoreDataSource
import com.example.mmrateconverter.data.remote.RemoteDataSource
import com.example.mmrateconverter.data.repository.ExchangeRateRepository
import com.example.mmrateconverter.data.repository.ExchangeRateRepositoryImpl
import com.example.mmrateconverter.data.repository.GoldPriceRepository
import com.example.mmrateconverter.data.repository.GoldPriceRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    // Remote Data Source
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideRemoteDataSource(firestore: FirebaseFirestore): RemoteDataSource {
        return FirebaseFirestoreDataSource(firestore)
    }

    // Local Data Source
    @Provides
    @Singleton
    fun provideLocalDataSource(rateDao: RateDao, goldDao: GoldDao): LocalDataSource {
        return LocalDataSourceImpl(rateDao, goldDao)
    }

    // Repository
    @Provides
    @Singleton
    fun provideExchangeRateRepository(
        remote: RemoteDataSource,
        local: LocalDataSource
    ): ExchangeRateRepository {
        return ExchangeRateRepositoryImpl(remote, local)
    }

    @Provides
    @Singleton
    fun provideGoldPriceRepository(
        remote: RemoteDataSource,
        local: LocalDataSource
    ): GoldPriceRepository {
        return GoldPriceRepositoryImpl(remote, local)
    }
}