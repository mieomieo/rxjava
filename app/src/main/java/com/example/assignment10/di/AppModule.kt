package com.example.assignment10.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.assignment10.database.ContactDatabase
import com.example.assignment10.database.dao.ContactDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContactDatabase(application: Application) : ContactDatabase {
        return Room.databaseBuilder(
            application.applicationContext,ContactDatabase::class.java,"contact_db"
        ).build()
    }
    @Provides
    @Singleton
    fun provideContactDao(db:ContactDatabase):ContactDao{
        return db.contactDao()
    }
}