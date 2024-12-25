package ir.elkiy.noteapp.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ir.elkiy.noteapp.R
import ir.elkiy.noteapp.adapter.Recycler.NotesAdapter
import ir.elkiy.noteapp.data.DBHelper
import ir.elkiy.noteapp.data.dao.NotesDao
import ir.elkiy.noteapp.databinding.ActivityMainBinding

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


      /*  binding.txtReBin.setOnClickListener {

        }*/
        binding.imgAddNote.setOnClickListener {
            val intent = Intent(this, AddNotesActivity::class.java)
            intent.putExtra("newNotes", true)
            startActivity(intent)
        }

        binding.icMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.navView,true)
        }
        binding.navView.setNavigationItemSelectedListener {item->
            when (item.itemId) {
                R.id.nav_recycler -> {
                    val intent = Intent(this,RecycleBinActivity::class.java)
                    startActivity(intent)
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                    true
                }
                R.id.nav_share -> {
                    shareLink(this, url = "https://cafebazaar.ir/app/?id=info.alirezaahmadi.eee&ref=share")
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                    true
                }
                R.id.nav_start -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                    true
                }
                R.id.exit -> {
                    finish()
                    true
                }
                else -> false
            }
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


fun shareLink(context: Context, url: String) {
    try {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share link via"))
    } catch (e: Exception) {
        Toast.makeText(context, "خطا", Toast.LENGTH_SHORT).show()
    }
}

fun comments(context: Context) {
    val intent = Intent(Intent.ACTION_EDIT).apply {
        setData(Uri.parse("bazaar://details?id=" + context.packageName))
        setPackage("com.farsitel.bazaar")
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "خطا", Toast.LENGTH_SHORT).show()
    }

}