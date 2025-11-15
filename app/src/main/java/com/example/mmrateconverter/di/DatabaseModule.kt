package com.example.mmrateconverter.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.mmrateconverter.data.local.database.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // App တစ်ခုလုံးအတွက် Singleton ဖြစ်ကြောင်း ကြေညာ
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "rate_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRateDao(database: AppDatabase) = database.rateDao()
    @Provides
    @Singleton
    fun provideGoldDao(database: AppDatabase) = database.goldDao()
}