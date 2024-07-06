package com.harjot.tododatabasetask

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.RoomDatabase
import com.harjot.tododatabasetask.databinding.ActivityMainBinding
import com.harjot.tododatabasetask.databinding.DialogLayoutBinding

class MainActivity : AppCompatActivity(),InterfaceClass {
    lateinit var binding: ActivityMainBinding
    lateinit var todoDatabase: TodoDatabase
    var arrayList = ArrayList<TodoEntity>()
    lateinit var adapterClass : AdapterClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        todoDatabase = TodoDatabase.getDatabase(this)
        adapterClass = AdapterClass(arrayList,this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        binding.recyclerView.adapter = adapterClass

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.fabAdd.setOnClickListener {
            dialog()
        }
        getData()
    }
    override fun onEditCLick(position: Int) {
        dialog(position)
    }

    override fun onDeleteCLick(position: Int) {
        var alertDialog = android.app.AlertDialog.Builder(this)
        alertDialog.setTitle("Delete Item")
        alertDialog.setMessage("Do you want to delete the item?")
        alertDialog.setCancelable(false)
        alertDialog.setNegativeButton("No") { _, _ ->
            alertDialog.setCancelable(true)
        }
        alertDialog.setPositiveButton("Yes") { _, _ ->
            Toast.makeText(this, "The item is  deleted", Toast.LENGTH_SHORT).show()
            todoDatabase.todoDao().delete(arrayList[position])
            adapterClass.notifyDataSetChanged()
            getData()
        }
        alertDialog.show()
    }

    fun dialog(position: Int = -1){
        var dialogBinding = DialogLayoutBinding.inflate(layoutInflater)
        var dialog = Dialog(this).apply {
            setContentView(dialogBinding.root)
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            if (position == -1){
                dialogBinding.btnAdd.setText("Add")
                dialogBinding.tvText.setText("Add Item")
            }else{
                dialogBinding.tvText.setText("Update Item")
                dialogBinding.btnAdd.setText("Update")
                dialogBinding.etTitle.setText(arrayList[position].title)
            }
            dialogBinding.btnAdd.setOnClickListener {
                if (dialogBinding.etTitle.text.toString().trim().isNullOrEmpty()){
                    dialogBinding.etTitle.error = "Enter Name"
                }
//                else if(dialogBinding.rbHigh.isSelected == false &&
//                    dialogBinding.rbMedium.isSelected == false &&
//                    dialogBinding.rbLow.isSelected == false){
//                    Toast.makeText(this@MainActivity, "Select Priority", Toast.LENGTH_SHORT).show()
//                }
                else{
                    if (position>-1){
                        //Update List
                        todoDatabase.todoDao().update(
                            TodoEntity(
                                id = arrayList[position].id,
                                title = dialogBinding.etTitle.text.toString()
                            )
                        )
                    }else{
                        //Add List
                        todoDatabase.todoDao().insertData(
                            TodoEntity(
                            title = dialogBinding.etTitle.text.toString()
                            )
                        )
                    }
                    adapterClass.notifyDataSetChanged()
                    getData()
                    dismiss()
                }
            }
            show()
        }
    }
    fun getData(){
        arrayList.clear()
        arrayList.addAll(todoDatabase.todoDao().getList())
        adapterClass.notifyDataSetChanged()
    }
}