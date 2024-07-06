package com.harjot.tododatabasetask

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.harjot.tododatabasetask.Interface.InterfaceClass
import com.harjot.tododatabasetask.Notes.NotesActivity
import com.harjot.tododatabasetask.databinding.ActivityMainBinding
import com.harjot.tododatabasetask.databinding.DialogLayoutBinding

class MainActivity : AppCompatActivity(), InterfaceClass {
    lateinit var binding: ActivityMainBinding
    lateinit var todoDatabase: TodoDatabase
    var arrayList = ArrayList<TodoEntity>()
    lateinit var adapterClass : AdapterClass
    var todoEntity = TodoEntity()
    private val TAG = "MainActivity"
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
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.rbAll->getData()
                R.id.rbHigh->getHighData()
                R.id.rbMedium->getMediumData()
                R.id.rbLow->getLowData()
            }
        }
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

    override fun listCLick(position: Int) {
        var intent = Intent(this, NotesActivity::class.java)
        var modelString = Gson().toJson(arrayList[position])
        intent.putExtra("notes",modelString)
        startActivity(intent)
    }

    fun dialog(position: Int = -1){
        var dialogBinding = DialogLayoutBinding.inflate(layoutInflater)
        Dialog(this).apply {
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
                dialogBinding.etDescription.setText(arrayList[position].description)
            }
            dialogBinding.btnAdd.setOnClickListener {
                if (dialogBinding.etTitle.text.toString().trim().isNullOrEmpty()){
                    dialogBinding.etTitle.error = "Enter Title"
                }else if (dialogBinding.etDescription.text.toString().trim().isNullOrEmpty()) {
                    dialogBinding.etDescription.error = "Enter Description"
                }
                else if(dialogBinding.rbHigh.isChecked == false &&
                    dialogBinding.rbMedium.isChecked == false &&
                    dialogBinding.rbLow.isChecked == false){
                    Toast.makeText(this@MainActivity, "Select Priority", Toast.LENGTH_SHORT).show()
                }
                else{
                    if (position>-1){
                        //Update List
                        todoDatabase.todoDao().update(
                            TodoEntity(
                                id = arrayList[position].id,
                                title = dialogBinding.etTitle.text.toString(),
                                description = dialogBinding.etDescription.text.toString()
                            )
                        )
                    }else{
                        //Add List
                        var counter=0
                        when(dialogBinding.radioGroup.checkedRadioButtonId){
                            R.id.rbHigh-> counter = 1
                            R.id.rbMedium-> counter = 2
                            R.id.rbLow-> counter = 3
                        }
                        todoDatabase.todoDao().insertData(
                            TodoEntity(
                                title = dialogBinding.etTitle.text.toString(),
                                description = dialogBinding.etDescription.text.toString(),
                                status = counter
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
    fun getHighData(){
        arrayList.clear()
        arrayList.addAll(todoDatabase.todoDao().getHighList())
        adapterClass.notifyDataSetChanged()
    }
    fun getMediumData(){
        arrayList.clear()
        arrayList.addAll(todoDatabase.todoDao().getMediumList())
        adapterClass.notifyDataSetChanged()
    }
    fun getLowData(){
        arrayList.clear()
        arrayList.addAll(todoDatabase.todoDao().getLowList())
        adapterClass.notifyDataSetChanged()
    }

}