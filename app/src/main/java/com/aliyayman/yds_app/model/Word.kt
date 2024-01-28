package com.aliyayman.yds_app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word")
data class Word(
    @ColumnInfo(name = "ing")
    val ing : String?,
    @ColumnInfo(name = "tc")
    val tc : String?,
    @ColumnInfo(name = "isFavorite")
    val isFavorite : Boolean?,
    @ColumnInfo(name = "categoryId")
    val categoryId: Int?,
) {
    @PrimaryKey(autoGenerate = true)
    var id:  Int=0
}