package com.example.pertemuan12

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "note_table")
data class ToDo (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,

    @ColumnInfo(name = "toDo_name")
    var toDo_name: String,

    @ColumnInfo(name = "toDo_status")
    var toDo_status: String,

    @ColumnInfo(name = "toDo_date")
    var toDo_date: Date
    )