package ir.elkiy.noteapp.adapter.Recycler

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
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
import ir.elkiy.noteapp.databinding.ListItemBinBinding

class RecycleBinAdapter(
    private val context: Context,
    private val dao: NotesDao,
) : RecyclerView.Adapter<RecycleBinAdapter.RecycleViewHolder>() {

    private val allData = dao.getNotesForRecycler(DBHelper.TRUE_STATE)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecycleViewHolder(
            ListItemBinBinding.inflate(LayoutInflater.from(context), parent, false)

        )

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.setDta((allData[position]))
    }

    override fun getItemCount(): Int = allData.size



    inner class RecycleViewHolder(
        private val binding: ListItemBinBinding,

        ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun setDta(data: RecyclerNOtesModel) {
            binding.txtTitleNote.text = data.title

            binding.imgDeletNotes.setOnClickListener {
                AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setTitle("حذف یاداشت")
                    .setMessage("آیا میخواهید یاداشت برای همیشه حذف شود؟")
                    .setIcon(R.drawable.ic_delete)
                    .setNegativeButton("بله") { _, _ ->
                        val result = dao.deleteNotes(data.id)
                        if (result) {
                            Snackbar.make(binding.root,"sd",Snackbar.LENGTH_SHORT)
                                .setText("یاداشت برای همیشه حذف شد")
                                .setTextColor(Color.WHITE)
                                .setBackgroundTint(Color.RED)
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

            binding.imgRestoreNotes.setOnClickListener {
                AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setTitle("بازگردانی یاداشت")
                    .setMessage("آیا میخواهید یاداشت بازگردانی شود؟")
                    .setIcon(R.drawable.ic_delete)

                    .setNegativeButton("بله") { _, _ ->
                        val result = dao.editNotes(data.id, DBHelper.FALSE_STATE  )
                        if (result) {
                            Snackbar.make(binding.root,"sd",Snackbar.LENGTH_SHORT)
                                .setText("یاداشت بازگردانی شد")
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

        }

    }

    private fun showText(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }


}