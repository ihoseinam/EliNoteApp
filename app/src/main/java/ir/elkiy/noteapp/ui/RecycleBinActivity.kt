package ir.elkiy.noteapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.elkiy.noteapp.adapter.Recycler.RecycleBinAdapter
import ir.elkiy.noteapp.data.DBHelper
import ir.elkiy.noteapp.data.dao.NotesDao
import ir.elkiy.noteapp.databinding.ActivityRecycleBinBinding

class RecycleBinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecycleBinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecycleBinBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initRecycler()
        binding.imageView.setOnClickListener { finish() }
    }

    private fun initRecycler() {
        val dao = NotesDao(DBHelper(this))
        val adapter = RecycleBinAdapter(this, dao)
        binding.recyclerNote.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerNote.adapter = adapter


    }
}