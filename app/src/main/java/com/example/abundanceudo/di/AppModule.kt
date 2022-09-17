package com.example.abundanceudo.di

import com.example.abundanceudo.feature_bmi.data.repository.BmiRepositoryImpl
import com.example.abundanceudo.feature_bmi.domain.repository.BmiRepository
import com.example.abundanceudo.feature_bmi.domain.use_case.BmiUseCases
import com.example.abundanceudo.feature_bmi.domain.repository.AdsRepository
import com.example.abundanceudo.feature_bmi.data.repository.AdsRepositoryImpl
import com.example.abundanceudo.feature_bmi.domain.use_case.AdsUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBmiRepository(): BmiRepository {
        return BmiRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideAdRepository(): AdsRepository {
        return AdsRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideBmiUseCases(bmiRepository: BmiRepository): BmiUseCases {
        return BmiUseCases(bmiRepository)
    }

    @Provides
    @Singleton
    fun provideAdsUseCases(adsRepository: AdsRepository): AdsUseCases {
        return AdsUseCases(adsRepository)
    }
}
