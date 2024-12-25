package ir.elkiy.noteapp.adapter.Recycler

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ir.elkiy.noteapp.R
import ir.elkiy.noteapp.data.DBHelper
import ir.elkiy.noteapp.data.dao.NotesDao
import ir.elkiy.noteapp.data.model.RecyclerNOtesModel
import ir.elkiy.noteapp.databinding.ListItemNoteBinding
import ir.elkiy.noteapp.ui.AddNotesActivity

class NotesAdapter(
    private val context: Context,
    private val dao: NotesDao,
) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private var allData: ArrayList<RecyclerNOtesModel>

    init {
        allData = dao.getNotesForRecycler(DBHelper.FALSE_STATE)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NotesViewHolder(
            ListItemNoteBinding.inflate(LayoutInflater.from(context), parent, false)

        )

    override fun getItemCount(): Int = allData.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.setDta(allData[position])
    }


    inner class NotesViewHolder(
        private val binding: ListItemNoteBinding,
        ) : RecyclerView.ViewHolder(binding.root) {
        fun setDta(data: RecyclerNOtesModel) {
            binding.txtTitleNote.text = data.title
            binding.imgDeletNotes.setOnClickListener {


                AlertDialog.Builder(ContextThemeWrapper(context, R.style.customAlertDialog))
                    .setCancelable(false)
                    .setTitle("حذف یاداشت")
                    .setMessage("آیا میخواهید یاداشت به سطل زباله منتقل شود؟")
                    .setIcon(R.drawable.ic_delete)

                    .setNegativeButton("بله") { _, _ ->
                        val result = dao.editNotes(data.id, DBHelper.TRUE_STATE)
                        if (result) {
                            Snackbar.make(binding.root,"sd",Snackbar.LENGTH_SHORT)
                                .setText("یاداشت به سطل زباله منتقل شد")
                                .setTextColor(Color.WHITE)
                                .setBackgroundTint(Color.DKGRAY)
                                .show()
                            allData.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                        } else {
                            showText("خطا")
                        }
                    }
                    .setPositiveButton("خیر") { _, _ -> }
                    .create()
                    .show()

            }
            binding.imgSharedNotes.setOnClickListener {
                shareText(context, "${data.title}\n\n${data.body}")
            }
            binding.root.setOnClickListener {
                val intent = Intent(context, AddNotesActivity::class.java)
                intent.putExtra("notesId", data.id)
                context.startActivity(intent)

            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeData(data: ArrayList<RecyclerNOtesModel>) {
        allData = data
        notifyDataSetChanged()

    }

    private fun showText(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }


    fun shareText(context: Context, text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(intent, "اشتراک‌گذاری متن با:"))
    }


}