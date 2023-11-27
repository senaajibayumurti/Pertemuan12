package com.example.pertemuan12

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pertemuan12.databinding.AdapterToDoBinding

typealias OnClick = (ToDo) -> Unit
class ToDoAdapter (private var toDoList: List<ToDo>, private val onClick: OnClick) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {
    fun updateData(newContacts: List<ToDo>): Int {
        toDoList = newContacts
        notifyDataSetChanged()
        return toDoList.size
    }

    inner class ToDoViewHolder(val binding: AdapterToDoBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(item : ToDo){
            val statusArray = itemView.context.resources.getStringArray(R.array.status)
            with(binding){
                txtToDoStatus.text = item.toDo_status
                txtToDoName.text = item.toDo_name
                txtToDoDate.text = item.toDo_date.toString()
                btnDone.setOnClickListener {

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = AdapterToDoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return toDoList.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(toDoList[position])
    }
}