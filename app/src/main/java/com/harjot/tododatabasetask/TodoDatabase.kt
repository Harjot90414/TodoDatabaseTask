package com.harjot.tododatabasetask

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities =[TodoEntity::class], version = 1, exportSchema = true)
abstract class TodoDatabase:RoomDatabase() {

    abstract fun todoDao():TodoDao

    companion object{
        private var todoDatabase : TodoDatabase?= null

        @Synchronized
        fun getDatabase(context: Context):TodoDatabase{
            if(todoDatabase == null){
                todoDatabase = Room.databaseBuilder(context,
                    TodoDatabase::class.java,
                    "TodoDatabase")
                    .allowMainThreadQueries()
                    .build()
            }
            return todoDatabase!!
        }
    }
}