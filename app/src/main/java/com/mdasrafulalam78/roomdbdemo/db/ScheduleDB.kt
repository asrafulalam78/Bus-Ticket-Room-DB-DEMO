package com.mdasrafulalam78.roomdbdemo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mdasrafulalam78.roomdbdemo.daos.ScheduleDao
import com.mdasrafulalam78.roomdbdemo.daos.UserDao
import com.mdasrafulalam78.roomdbdemo.model.BusSchedule
import com.mdasrafulalam78.roomdbdemo.model.Cart
import com.mdasrafulalam78.roomdbdemo.model.UserModel

@Database(entities = [BusSchedule::class,Cart::class, UserModel::class], version = 1)
abstract class ScheduleDB : RoomDatabase(){
    abstract fun getScheduleDao() : ScheduleDao
    abstract fun userDao() : UserDao

    companion object {
        private var db: ScheduleDB? = null

//        private val migration_1_2 : Migration = object : Migration(1,2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("alter table 'tbl_schedule' add column 'image' text")
//            }
//        }
//
//        private val migration_2_3 : Migration = object : Migration(2,3) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE `tbl_cart` (`id` LONG, `name` TEXT, `from` TEXT, `to` TEXT, `departure_date` TEXT, `departure_time` TEXT, `bus_type` TEXT,`image` TEXT, `fare` INTEGER, " +
//                        "PRIMARY KEY(`id`))")
//                database.execSQL("alter table 'tbl_schedule' add column 'fare' INTEGER DEFAULT 0 ")
//            }
//        }

        fun getDB(context: Context) : ScheduleDB {
            if (db == null) {
                db = Room.databaseBuilder(
                    context.applicationContext,
                    ScheduleDB::class.java,
                    "schedule_db"
                ).build()
                return db!!
            }
            return db!!
        }
    }
}