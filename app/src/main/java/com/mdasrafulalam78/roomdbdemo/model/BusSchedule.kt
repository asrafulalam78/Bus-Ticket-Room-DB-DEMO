package com.mdasrafulalam78.roomdbdemo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

@Entity(tableName = "tbl_schedule")
data class BusSchedule(
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
    var favorite: Boolean = false,
    var image: String? = null,
    val fare:Int = 0
)


