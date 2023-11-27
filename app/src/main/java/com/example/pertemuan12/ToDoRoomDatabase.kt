package com.example.pertemuan12

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [ToDo::class], version = 1, exportSchema = false)
abstract class ToDoRoomDatabase : RoomDatabase(){
    abstract fun noteDao():ToDoDao?
    companion object {
        @Volatile
        private var INSTANCE: ToDoRoomDatabase? = null
        fun getDatabase(context: Context) : ToDoRoomDatabase?{
            if (INSTANCE == null){
                synchronized(ToDoRoomDatabase::class.java){
                    INSTANCE = databaseBuilder(context.applicationContext,
                    ToDoRoomDatabase::class.java, "todo_database").build()
                }
            }
            return INSTANCE
        }
    }
}