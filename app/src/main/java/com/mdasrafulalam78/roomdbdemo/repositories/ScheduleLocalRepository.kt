package com.mdasrafulalam78.roomdbdemo.repositories

import androidx.lifecycle.LiveData
import com.mdasrafulalam78.roomdbdemo.daos.ScheduleDao
import com.mdasrafulalam78.roomdbdemo.model.BusSchedule
import com.mdasrafulalam78.roomdbdemo.model.Cart

class ScheduleLocalRepository(private val dao: ScheduleDao) {
    suspend fun addSchedule(busSchedule: BusSchedule) = dao.addSchedule(busSchedule)

    suspend fun addToCart(cart: Cart) = dao.addToCart(cart)

    suspend fun updateSchedule(busSchedule: BusSchedule) = dao.updateSchedule(busSchedule)

    suspend fun deleteSchedule(busSchedule: BusSchedule) = dao.deleteSchedule(busSchedule)

    fun getAllSchedules() : LiveData<List<BusSchedule>> = dao.getAllSchedules()
    fun getAllCarts() : LiveData<List<Cart>> = dao.getAllCarts()

    fun getScheduleById(id: Long) : LiveData<BusSchedule> = dao.getScheduleById(id)

    fun getScheduleByFavourite():LiveData<List<BusSchedule>> = dao.getScheduleByFavourite()
}