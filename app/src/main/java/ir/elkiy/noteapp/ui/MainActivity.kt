package ir.elkiy.noteapp.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.size
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

        binding.imgAddNote.setOnClickListener {
            val intent = Intent(this, AddNotesActivity::class.java)
            intent.putExtra("newNotes", true)
            startActivity(intent)
        }

        binding.icMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.navView, true)
        }
        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_recycler -> {
                    val intent = Intent(this, RecycleBinActivity::class.java)
                    startActivity(intent)
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                    true
                }

                R.id.nav_share -> {
                    shareLink(this, url = "https://cafebazaar.ir/app/?id=${packageName}&ref=share")
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                    true
                }

                R.id.nav_start -> {
                    comments(this)
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
        adapter = NotesAdapter(this, dao)

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