package com.mdasrafulalam78.roomdbdemo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_cart")
data class Cart (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val name: String,
    val from: String,
    val to: String,
    @ColumnInfo(name = "departure_date")
    val departureDate: String,
    @ColumnInfo(name = "departure_time")
    val departureTime: String,
    @ColumnInfo(name = "bus_type")
    val busType: String,
    var image: String? = null,
    val fare:Int,
)