package com.example.pertemuan12

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(toDo: ToDo)

    @Update
    fun update(toDo: ToDo)

    @Delete
    fun delete(toDo: ToDo)

    @get:Query("SELECT * from todo_table ORDER BY id ASC")
    val allToDo: LiveData<List<ToDo>>
}