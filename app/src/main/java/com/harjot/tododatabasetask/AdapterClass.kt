package com.harjot.tododatabasetask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class AdapterClass(var arrayList: ArrayList<TodoEntity>,var interfaceClass: InterfaceClass):
    RecyclerView.Adapter<AdapterClass.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var name = view.findViewById<TextView>(R.id.tvName)
        var edit = view.findViewById<ImageButton>(R.id.ibEdit)
        var update = view.findViewById<ImageButton>(R.id.ibDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var initview = LayoutInflater.from(parent.context).inflate(R.layout.layout_item,parent,false)
        return ViewHolder(initview)    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.setText(arrayList[position].name)
        holder.edit.setOnClickListener {
            interfaceClass.onEditCLick(position)
        }
        holder.update.setOnClickListener {
            interfaceClass.onDeleteCLick(position)
        }
    }
}