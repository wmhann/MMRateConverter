package com.example.mmrateconverter.di

import com.example.mmrateconverter.data.repository.ExchangeRateRepository
import com.example.mmrateconverter.domain.usecase.GetLatestRatesUseCase
import com.example.mmrateconverter.domain.usecase.ToggleFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetLatestRatesUseCase(repository: ExchangeRateRepository): GetLatestRatesUseCase {
        return GetLatestRatesUseCase(repository)
    }

    @Provides
    fun provideToggleFavoriteUseCase(repository: ExchangeRateRepository): ToggleFavoriteUseCase {
        return ToggleFavoriteUseCase(repository)
    }


}