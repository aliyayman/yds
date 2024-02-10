package com.aliyayman.yds_app.model

import androidx.room.ColumnInfo

data class WordRecyler(
    val id:Int,
    val ing : String?,
    val tc : String?,
    val isFavorite : Boolean?,
    val categoryId: Int?,
    var isVisibility:Boolean
)
