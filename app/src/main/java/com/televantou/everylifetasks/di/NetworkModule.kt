package com.televantou.everylifetasks.di

import com.televantou.everylifetasks.api.ELApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * Created by Eirini Televantou on 03/04/2021 for EveryLife.
 */

//Network module to be injected using Dagger
@InstallIn(ApplicationComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideService(): ELApi {
        return ELApi.create()
    }
}
