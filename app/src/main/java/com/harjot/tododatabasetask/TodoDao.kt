package com.harjot.tododatabasetask

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.harjot.tododatabasetask.Notes.NotesEntity


@Dao
interface TodoDao {
    @Insert
    fun insertData(todoEntity: TodoEntity)

    @Insert
    fun insertItem(notesEntity: NotesEntity)

    @Query("SELECT * FROM TodoEntity")
    fun getList():List<TodoEntity>

    @Query("SELECT * FROM TodoEntity WHERE status = 1")
    fun getHighList():List<TodoEntity>

    @Query("SELECT * FROM TodoEntity WHERE status = 2")
    fun getMediumList():List<TodoEntity>

    @Query("SELECT * FROM TodoEntity WHERE status = 3")
    fun getLowList():List<TodoEntity>


    @Query("SELECT * FROM NotesEntity WHERE notesId =:notesId")
    fun getListItem(notesId:Int):List<NotesEntity>

    @Delete
    fun delete(todoEntity: TodoEntity)

    @Update
    fun update(todoEntity: TodoEntity)
}