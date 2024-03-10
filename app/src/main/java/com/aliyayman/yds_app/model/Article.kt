package com.aliyayman.yds_app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article")
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id:  Int=0,
    val name : String?,
    val text : String?,
){}
