package com.harjot.tododatabasetask.Notes

import android.app.Dialog
import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.view.WindowManager.LayoutParams
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.harjot.tododatabasetask.TodoDatabase
import com.harjot.tododatabasetask.TodoEntity
import com.harjot.tododatabasetask.databinding.ActivityNotesBinding
import com.harjot.tododatabasetask.databinding.NotesDialogLayoutBinding

class NotesActivity : AppCompatActivity() {
    lateinit var binding: ActivityNotesBinding
    var todoEntity = TodoEntity()
    var array = arrayListOf<NotesEntity>()
    var notesAdapter = NotesAdapter(array)
    lateinit var todoDatabase:TodoDatabase
    lateinit var layoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        todoDatabase = TodoDatabase.getDatabase(this)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        intent?.extras?.let {
            var notes = it.getString("notes")
            todoEntity = Gson().fromJson(notes, TodoEntity::class.java)
            binding.tvTitle.setText(todoEntity.title)
            binding.tvDescription.setText(todoEntity.description)
        }

        layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = notesAdapter

        binding.fabAdd.setOnClickListener {
            Dialog(this).apply {
                var dialogBinding = NotesDialogLayoutBinding.inflate(layoutInflater)
                setContentView(dialogBinding.root)
                window?.setLayout(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )
                dialogBinding.btnAddDialog.setOnClickListener {
                    if(dialogBinding.etItem.text.toString().trim().isNullOrEmpty()){
                        dialogBinding.etItem.error = "Enter Item Name"
                    }else{
                        todoDatabase.todoDao().insertItem(
                            NotesEntity(
                            item = dialogBinding.etItem.text.toString(),
                            check = dialogBinding.checkBox.isChecked,
                            notesId = todoEntity.id
                            )
                        )
                        dismiss()
                    }
                    notesAdapter.notifyDataSetChanged()
                    getItemData()
                }
                show()
            }
        }
        getItemData()
    }
    fun getItemData(){
        array.clear()
        array.addAll(
            todoDatabase.todoDao().getListItem(todoEntity.id)
        )
        notesAdapter.notifyDataSetChanged()
    }
}