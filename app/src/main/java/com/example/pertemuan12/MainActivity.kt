package com.example.pertemuan12

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.pertemuan12.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var mToDoDao: ToDoDao
    private lateinit var executorService: ExecutorService
    private var updateId : Int = 0
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = ToDoRoomDatabase.getDatabase(this)
        mToDoDao = db!!.noteDao()!!

        with(binding){
            btnAdd.setOnClickListener {
                insert(
                    ToDo(
                    toDo_name = etTitle.text.toString(),
                    toDo_date = etDesc.text.toString())
                )
                setEmptyField()
            }
            lvItem.setOnItemClickListener {
                adapterView, _, i, _->
                val item = adapterView.adapter.getItem(i) as ToDo
                updateId = item.id
                etTitle.setText(item.toDo_name)
                etDesc.setText(item.toDo_date)
            }
            btnDelete.setOnClickListener {
                update(
                    ToDo(
                    id = updateId,
                    toDo_name = etTitle.text.toString(),
                    toDo_date = etDesc.text.toString()))
                updateId = 0
                setEmptyField()
            }
            lvItem.onItemClickListener =
                AdapterView.OnItemClickListener{
                    adapterView: AdapterView<*>?, view: View?, i: Int, l: Long ->
                    val item = adapterView!!.adapter.getItem(i) as ToDo
                    delete(item)
                    true
                }
        }
    }

    override fun onResume() {
        super.onResume()
        getAllNotes()
    }

    private fun getAllNotes(){
        mToDoDao.allToDo.observe(this){
            notes ->
            val adapter: ArrayAdapter<ToDo> =
                ArrayAdapter<ToDo>(this, android.R.layout.simple_list_item_1,
                notes)
            binding.lvItem.adapter = adapter
        }
    }

    private fun insert(toDo: ToDo){
        executorService.execute{
            mToDoDao.insert(toDo)
        }
    }

    private fun update(toDo: ToDo){
        executorService.execute{
            mToDoDao.update(toDo)
        }
    }

    private fun delete(toDo: ToDo){
        executorService.execute{
            mToDoDao.delete(toDo)
        }
    }

    private fun setEmptyField(){
        with(binding){
            etTitle.setText("")
            etDesc.setText("")
        }
    }
}