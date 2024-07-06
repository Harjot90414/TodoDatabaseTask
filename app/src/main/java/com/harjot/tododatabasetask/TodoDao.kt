package com.harjot.tododatabasetask

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface TodoDao {
    @Insert
    fun insertData(todoEntity: TodoEntity)

    @Query("SELECT * FROM TodoEntity")
    fun getList():List<TodoEntity>

    @Delete
    fun delete(todoEntity: TodoEntity)

    @Update
    fun update(todoEntity: TodoEntity)
}