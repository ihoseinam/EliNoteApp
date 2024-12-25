package ir.hoseinahmadi.noteapp.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ir.hoseinahmadi.noteapp.data.DBHelper
import ir.hoseinahmadi.noteapp.data.dao.NotesDao
import ir.hoseinahmadi.noteapp.data.model.DBNotesModel
import ir.hoseinahmadi.noteapp.databinding.ActivityAddNotesBinding
import ir.hoseinahmadi.noteapp.utils.PersianDate

class AddNotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNotesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val type = intent.getBooleanExtra("newNotes", false)
        val id = intent.getIntExtra("notesId", 0)

        val dao = NotesDao(DBHelper(this))
        if (type) {
            binding.txtDate.text = getDate()
        } else {
            val notes = dao.getNotesById(id)
            val edit = Editable.Factory()
            binding.edtTitleNote.text = edit.newEditable(notes.title)
            binding.edtDetailNote.text = edit.newEditable(notes.detail)
            binding.txtDate.text = notes.date
        }

        binding.btnSave.setOnClickListener {
            val title = binding.edtTitleNote.text.toString()
            val detail = binding.edtDetailNote.text.toString()
            if (title.isNotEmpty()) {
                val notes = DBNotesModel(0, title, detail, DBHelper.FALSE_STATE, getDate())
                val result = if (type)
                    dao.saveNotes(notes)
                else
                    dao.editNotes(id, notes)
                if (result) {
                    Toast.makeText(this, "با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "خطا در ذخیره سازی یاداشت", Toast.LENGTH_SHORT).show()
                }
            } else
                snack("عنوان نمیتواند خالی باشد")
        }
        binding.btnCancel.setOnClickListener { finish() }

    }

    private fun getDate(): String {

        val date = PersianDate()

        val currentDate = "${date.year}/${date.month}/${date.day}"
        val currentTime = "${date.hour}:${date.min}:${date.second}"

        return "$currentDate | $currentTime"

    }



private fun snack(text: String){
    val snake = Snackbar.make(binding.root, "عنوان ", Snackbar.LENGTH_SHORT)
    snake.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
    snake.setText(text)
    snake.setBackgroundTint(Color.DKGRAY)
    snake.setTextColor(Color.WHITE)
    snake.show()
}

}



