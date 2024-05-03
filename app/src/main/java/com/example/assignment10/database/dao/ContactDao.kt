package com.example.assignment10.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.assignment10.model.Contact
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contact: Contact)

    @Update
    fun updateContact(contact: Contact)

    @Delete
    fun deleteContact(contact: Contact)


    @Query("Select * from contact_table")
    fun getAllContact(): Flowable<List<Contact>>

//    @Query("SELECT * FROM contact_table WHERE name_col=:name")
//    fun getContactByName(name:String):LiveData<List<Contact>>
}