package com.f8fit.bambutestandroid.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.f8fit.bambutestandroid.data.dao.CartDao
import com.f8fit.bambutestandroid.data.dto.cartDto.CartItemEntity


@Database(entities = [CartItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}
