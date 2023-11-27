package com.example.pertemuan12

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pertemuan12.databinding.AdapterToDoBinding
import java.util.concurrent.ExecutorService

typealias OnClick = (ToDo) -> Unit
class ToDoAdapter (private var toDoList: List<ToDo>,
                   private val onClick: OnClick,
                   private val executorService: ExecutorService
): RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {
    lateinit var mToDoDao: ToDoDao

    inner class ToDoViewHolder(val binding: AdapterToDoBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(item : ToDo){
            with(binding){
                txtToDoStatus.text = item.toDo_status
                txtToDoName.text = item.toDo_name
                txtToDoDate.text = item.toDo_date.toString()
                btnDone.setOnClickListener {
                    delete(item)
                }
                itemView.setOnClickListener {
                    onClick(item)
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
        val sortedList = toDoList.sortedBy { it.toDo_status }
        holder.bind(sortedList[position])
    }

    private fun update(toDo: ToDo){
        executorService.execute{
            mToDoDao.update(toDo)
        }
    }

    fun delete(toDo: ToDo){
        executorService.execute{
            mToDoDao.delete(toDo)
        }
    }
}