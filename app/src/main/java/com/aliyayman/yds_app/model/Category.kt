package com.aliyayman.yds_app.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "category")
data class Category(
    @ColumnInfo(name = "categoryId")
    val categoryId : Int,
    @ColumnInfo(name = "name")
    val name : String?
) {
}