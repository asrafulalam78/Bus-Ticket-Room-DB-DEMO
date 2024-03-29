package com.mdasrafulalam78.roomdbdemo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_user")
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var userId: Long = 0,
    var email: String,
    var password: String
    )
