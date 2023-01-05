package com.mdasrafulalam78.roomdbdemo.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mdasrafulalam78.roomdbdemo.model.BusSchedule
import com.mdasrafulalam78.roomdbdemo.model.Cart

@Dao
interface ScheduleDao {
    @Insert
    suspend fun addSchedule(busSchedule: BusSchedule)

    @Insert
    suspend fun addToCart(cart: Cart)

    @Update
    suspend fun updateSchedule(busSchedule: BusSchedule)

    @Delete
    suspend fun deleteSchedule(busSchedule: BusSchedule)

    @Query("select * from tbl_schedule")
    fun getAllSchedules() : LiveData<List<BusSchedule>>

    @Query("select * from tbl_cart")
    fun getAllCarts() : LiveData<List<Cart>>

    @Query("select * from tbl_schedule where id = :id")
    fun getScheduleById(id: Long) : LiveData<BusSchedule>

    @Query("select * from tbl_schedule where favorite = 1")
    fun getScheduleByFavourite() : LiveData<List<BusSchedule>>
}