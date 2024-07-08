package com.harjot.tododatabasetask.Notes

import android.provider.ContactsContract.CommonDataKinds.Note
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harjot.tododatabasetask.Interface.NotesInterfaceClass
import com.harjot.tododatabasetask.R

class NotesAdapter(var array: ArrayList<NotesEntity>,var notesInterfaceClass: NotesInterfaceClass):
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var itemName = view.findViewById<TextView>(R.id.tvItemName)
        var checkBox = view.findViewById<CheckBox>(R.id.itemCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.notes_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = array.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemName.setText(array[position].item)
        holder.checkBox.isChecked = array[position].check?:false
//        holder.checkBox.setOnCheckedChangeListener { compoundButton, b ->
//            when(b){
//                true->{array[position].check = true
//                }
//                false-> {array[position].check = false
//                }
//            }
//        }
    }
}