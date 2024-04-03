package com.aliyayman.yds_app.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aliyayman.yds_app.model.Article
import com.aliyayman.yds_app.model.Category
import com.aliyayman.yds_app.model.Word

@Database(entities = [Category::class, Word::class,Article::class], version = 3)
abstract class myDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun wordDao(): WordDao
    abstract fun articleDao(): ArticleDao

    //Singleton
    companion object{
    @Volatile    private var instance : myDatabase? = null

        private val lock = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(lock){
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }

        private fun makeDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,myDatabase::class.java,"mydatabase")
            .build()
    }
}