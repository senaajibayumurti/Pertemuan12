package com.example.pertemuan12

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pertemuan12.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var mToDoDao: ToDoDao
    private lateinit var executorService: ExecutorService
    private lateinit var binding: ActivityMainBinding
    private var updateId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = ToDoRoomDatabase.getDatabase(this)
        mToDoDao = db!!.noteDao()!!

        with(binding){
            btnAdd.setOnClickListener{
                if (lvToDoForm.visibility == View.GONE){
                    openForm()
                }
            }
            btnSubmit.setOnClickListener {
                val dateString = etToDoDate.text.toString()

                if (btnSubmit.text.toString() == getString(R.string.add)) {
                    Log.d("InsertButtonClicked", "Insert button clicked. Will perform insert operation.")
                    insert(
                        ToDo(
                            toDo_name = etToDoName.text.toString(),
                            toDo_date = dateString,
                            toDo_status = statusDefaultValue(toDoDateString = dateString)
                        )
                    )
                    setEmptyField()
                } else if (btnSubmit.text.toString() == getString(R.string.save)){
                    Log.d("UpdateButtonClicked", "Update button clicked. Will perform update operation.")
                    update(
                        ToDo(
                            id = updateId,
                            toDo_name = etToDoName.text.toString(),
                            toDo_date = dateString,
                            toDo_status = statusDefaultValue(toDoDateString = dateString)
                        )
                    )
                    updateId = 0
                }
            }
            btnCloseForm.setOnClickListener{
                setEmptyField()
                lvToDoForm.visibility = View.GONE
                btnAdd.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getAllToDos()
    }

    private fun getAllToDos() {
        mToDoDao.allToDo.observe(this) { toDos ->
            val toDoAdapter = ToDoAdapter(toDos, { selectedToDo ->
                editForm(selectedToDo)
                update(selectedToDo)
            }, executorService)

            toDoAdapter.mToDoDao = mToDoDao

            with(binding) {
                rvToDo.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = toDoAdapter
                }
            }
        }
    }


    private fun editForm(toDo: ToDo?) {
        toDo?.let { selectedToDo ->
            with(binding) {
                btnAdd.visibility = View.GONE
                lvToDoForm.visibility = View.VISIBLE
                btnSubmit.text = getString(R.string.save)

                etToDoName.setText(selectedToDo.toDo_name)
                etToDoDate.setText(selectedToDo.toDo_date)
            }
        }
    }

    private fun openForm() {
        with(binding){
            btnAdd.visibility = View.GONE
            lvToDoForm.visibility = View.VISIBLE
            btnSubmit.text = getString(R.string.add)
        }
    }
    private fun statusDefaultValue(toDoDateString: String): String {
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val toDoDate = dateFormat.parse(toDoDateString)

        val millisecondsPerDay = 24 * 60 * 60 * 1000
        val difference = (toDoDate.time - dateFormat.parse(currentDate).time) / millisecondsPerDay

        return when {
            difference < 0 -> "Overdue"
            difference == 0L || difference == 1L -> "Due"
            difference >= 2L -> "Going"
            else -> "Going"
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

    private fun setEmptyField(){
        with(binding){
            etToDoName.setText("")
            etToDoDate.setText("")
        }
    }
}