package com.example.mmrateconverter.di

import com.example.mmrateconverter.data.local.TimestampDataSource
import com.example.mmrateconverter.data.local.TimestampDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {

    @Binds
    @Singleton
    abstract fun bindTimestampDataSource(
        timestampDataSourceImpl: TimestampDataSourceImpl
    ): TimestampDataSource
}