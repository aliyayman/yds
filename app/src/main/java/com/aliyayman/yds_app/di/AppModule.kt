package com.aliyayman.yds_app.di

import com.aliyayman.yds_app.repository.WordRepository
import com.aliyayman.yds_app.service.myDatabase
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
    fun provideMovieRepository(myDatabase: myDatabase): WordRepository{
        return WordRepository(myDatabase)
    }

}