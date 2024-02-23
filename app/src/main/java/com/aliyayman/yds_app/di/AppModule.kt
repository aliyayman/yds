package com.aliyayman.yds_app.di

import android.content.Context
import androidx.room.Room
import com.aliyayman.yds_app.repository.WordRepository
import com.aliyayman.yds_app.service.WordDao
import com.aliyayman.yds_app.service.myDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun injectRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,myDatabase::class.java,"mydatabase"
    ).build()

    @Provides
    @Singleton
    fun injectWordDao(database: myDatabase)=database.wordDao()

    @Provides
    @Singleton
    fun injectCategoryDao(database: myDatabase)=database.categoryDao()


    @Provides
    @Singleton
    fun provideMovieRepository(wordDao: WordDao): WordRepository{
        return WordRepository(wordDao)
    }

}