package com.harjot.tododatabasetask.Notes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.harjot.tododatabasetask.TodoEntity

@Entity(foreignKeys = arrayOf(
    ForeignKey(
        TodoEntity::class,
        parentColumns = ["id"],
        childColumns = ["notesId"]
        )
    )
)
data class NotesEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    var item:String?=null,
    var check:Boolean?=false,
    var notesId:Int?=0
)
