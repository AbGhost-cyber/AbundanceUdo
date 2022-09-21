package com.example.abundanceudo.di

import com.example.abundanceudo.featureBmi.data.repository.AdsRepositoryImpl
import com.example.abundanceudo.featureBmi.data.repository.BmiBitmapCacheImpl
import com.example.abundanceudo.featureBmi.data.repository.BmiRepositoryImpl
import com.example.abundanceudo.featureBmi.domain.repository.AdsRepository
import com.example.abundanceudo.featureBmi.domain.repository.BmiBitmapCache
import com.example.abundanceudo.featureBmi.domain.repository.BmiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class InterfaceModules {

    @Binds
    abstract fun bindBmiRepository(
        bmiRepositoryImpl: BmiRepositoryImpl
    ): BmiRepository

    @Binds
    abstract fun bindAdRepository(
        adsRepositoryImpl: AdsRepositoryImpl
    ): AdsRepository

    @Binds
    abstract fun bindBmiBitmapCache(
        bmiBitmapCacheImpl: BmiBitmapCacheImpl
    ): BmiBitmapCache
}
