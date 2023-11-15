package com.example.pertemuan12

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.pertemuan12.databinding.ActivityMainBinding
import java.util.concurrent.AbstractExecutorService
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var mNoteDao: NoteDao
    private lateinit var executorService: ExecutorService
    private var updateId : Int = 0
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNoteDao = db!!.noteDao()!!

        with(binding){
            btnAdd.setOnClickListener {
                insert(
                    Note(
                    title = etTitle.text.toString(),
                    description = etDesc.text.toString())
                )
                setEmptyField()
            }
            lvItem.setOnItemClickListener {
                adapterView, _, i, _->
                val item = adapterView.adapter.getItem(i) as Note
                updateId = item.id
                etTitle.setText(item.title)
                etDesc.setText(item.description)
            }
            btnUpdate.setOnClickListener {
                update(
                    Note(
                    id = updateId,
                    title = etTitle.text.toString(),
                    description = etDesc.text.toString()))
                updateId = 0
                setEmptyField()
            }
            lvItem.onItemClickListener =
                AdapterView.OnItemClickListener{
                    adapterView: AdapterView<*>?, view: View?, i: Int, l: Long ->
                    val item = adapterView!!.adapter.getItem(i) as Note
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
        mNoteDao.allNote.observe(this){
            notes ->
            val adapter: ArrayAdapter<Note> =
                ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1,
                notes)
            binding.lvItem.adapter = adapter
        }
    }

    private fun insert(note: Note){
        executorService.execute{
            mNoteDao.insert(note)
        }
    }

    private fun update(note: Note){
        executorService.execute{
            mNoteDao.update(note)
        }
    }

    private fun delete(note: Note){
        executorService.execute{
            mNoteDao.delete(note)
        }
    }

    private fun setEmptyField(){
        with(binding){
            etTitle.setText("")
            etDesc.setText("")
        }
    }
}