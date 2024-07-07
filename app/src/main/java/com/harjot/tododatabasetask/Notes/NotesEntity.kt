package com.harjot.tododatabasetask.Notes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.harjot.tododatabasetask.TodoEntity

@Entity(foreignKeys = [ForeignKey(
    TodoEntity::class,
    parentColumns = ["id"],
    childColumns = ["notesId"],
    onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["notesId"])]
)
data class NotesEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    var item:String?=null,
    var check:Boolean?=false,
    var notesId:Int?=0
)
