package ir.hoseinahmadi.noteapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ir.hoseinahmadi.noteapp.R
import ir.hoseinahmadi.noteapp.adapter.Recycler.NotesAdapter
import ir.hoseinahmadi.noteapp.data.DBHelper
import ir.hoseinahmadi.noteapp.data.dao.NotesDao
import ir.hoseinahmadi.noteapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: NotesDao
    private lateinit var adapter: NotesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_note)
        val fab = findViewById<FloatingActionButton>(R.id.img_add_note)

        var isFabVisible = true
        var lastDy = 0

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // بررسی جهت اسکرول
                if (dy > 0 && isFabVisible) {
                    // اگر اسکرول به پایین باشد و فب قابل مشاهده باشد، آن را مخفی کنید
                    fab.hide()
                    isFabVisible = false
                } else if (dy < 0 || !isFabVisible) {
                    // اگر اسکرول به بالا باشد یا فب مخفی باشد، آن را نمایش دهید
                    fab.show()
                    isFabVisible = true
                }

                // ذخیره آخرین مقدار dy
                lastDy = dy
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                // زمانی که اسکرول به پایان رسید و آخرین جهت اسکرول به بالا بوده، فب را نمایش دهید
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastDy < 0) {
                    fab.show()
                    isFabVisible = true
                }
            }
        })


        binding.txtReBin.setOnClickListener {
            val intent = Intent(this,RecycleBinActivity::class.java)
            startActivity(intent)
        }
        binding.imgAddNote.setOnClickListener {
            val intent = Intent(this, AddNotesActivity::class.java)
            intent.putExtra("newNotes", true)
            startActivity(intent)
        }

        initRecycler()

    }

    override fun onStart() {
        super.onStart()
        val data = dao.getNotesForRecycler(DBHelper.FALSE_STATE)
        adapter.changeData(data)

    }

    private fun initRecycler() {
        dao = NotesDao(DBHelper(this))
        adapter = NotesAdapter(this,  dao)

        binding.recyclerNote.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerNote.adapter = adapter

    }



}