package com.example.assignment10.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "contact_table")
class Contact(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,
    @ColumnInfo(name = "name_col") var name: String = "",
    @ColumnInfo(name = "email_col") var email: String = "",
    @ColumnInfo(name = "phone_number_col") var phoneNumber: String = ""
) : Parcelable