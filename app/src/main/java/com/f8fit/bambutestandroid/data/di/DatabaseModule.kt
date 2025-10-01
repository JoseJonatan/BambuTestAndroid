package com.f8fit.bambutestandroid.data.di

import android.content.Context
import androidx.room.Room
import com.f8fit.bambutestandroid.data.dao.CartDao
import com.f8fit.bambutestandroid.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Context): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "bambu_cart_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCartDao(db: AppDatabase): CartDao = db.cartDao()
}
