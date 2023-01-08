package com.mdasrafulalam78.roomdbdemo.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mdasrafulalam78.roomdbdemo.model.UserModel

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(userModel: UserModel) : Long

    @Delete
    suspend fun deleteUser(userModel: UserModel)

    @Update
    suspend fun updateUser(userModel: UserModel)

    @Query("select * from tbl_user where email = :email")
    suspend fun getUserByEmail(email: String) : UserModel?
}