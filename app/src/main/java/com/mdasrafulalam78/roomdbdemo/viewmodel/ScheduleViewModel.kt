package com.mdasrafulalam78.roomdbdemo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mdasrafulalam78.roomdbdemo.db.ScheduleDB
import com.mdasrafulalam78.roomdbdemo.model.BusSchedule
import com.mdasrafulalam78.roomdbdemo.model.Cart
import com.mdasrafulalam78.roomdbdemo.repositories.ScheduleLocalRepository
import kotlinx.coroutines.launch

class ScheduleViewModel(application: Application)
    : AndroidViewModel(application){
        private var repository: ScheduleLocalRepository
        init {
            val dao = ScheduleDB.getDB(application).getScheduleDao()
            repository = ScheduleLocalRepository(dao)
        }

    fun addSchedule(busSchedule: BusSchedule) {
        viewModelScope.launch {
            repository.addSchedule(busSchedule)
        }
    }
    fun addToCart(cart: Cart) {
        viewModelScope.launch {
            repository.addToCart(cart)
        }
    }

    fun updateSchedule(busSchedule: BusSchedule) {
        viewModelScope.launch {
            repository.updateSchedule(busSchedule)
        }
    }

    fun deleteSchedule(busSchedule: BusSchedule) {
        viewModelScope.launch {
            repository.deleteSchedule(busSchedule)
        }
    }

    fun getAllSchedules() : LiveData<List<BusSchedule>> = repository.getAllSchedules()
    fun getAllCarts() : LiveData<List<Cart>> = repository.getAllCarts()
    fun getScheduleByFavourite() : LiveData<List<BusSchedule>> = repository.getScheduleByFavourite()

    fun getScheduleById(id: Long) : LiveData<BusSchedule> = repository.getScheduleById(id)
}