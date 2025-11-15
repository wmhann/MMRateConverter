package com.example.mmrateconverter.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.mmrateconverter.data.repository.ExchangeRateRepository
import com.example.mmrateconverter.domain.usecase.CalculateExchangeAmountUseCase
import com.example.mmrateconverter.domain.usecase.GetLatestRatesUseCase
import com.example.mmrateconverter.domain.usecase.ToggleFavoriteUseCase

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

    @Provides
    fun provideCalculateExchangeAmountUseCase(): CalculateExchangeAmountUseCase {
        return CalculateExchangeAmountUseCase()
    }
}