package ir.hoseinahmadi.noteapp.data.dao

import android.content.ContentValues
import android.database.Cursor
import ir.hoseinahmadi.noteapp.data.DBHelper
import ir.hoseinahmadi.noteapp.data.model.DBNotesModel
import ir.hoseinahmadi.noteapp.data.model.RecyclerNOtesModel

class NotesDao(private val db: DBHelper) {
    private lateinit var cursor: Cursor
    private val contentValues = ContentValues()
    fun saveNotes(notes: DBNotesModel): Boolean {
        val database = db.writableDatabase
        setContentValues(notes)
        val result = database.insert(DBHelper.NOTES_TABLE, null, contentValues)
        database.close()
        return result > 0
    }


    fun editNotes(id: Int, state: String): Boolean {
        val database = db.writableDatabase
        contentValues.clear()
        contentValues.put(DBHelper.NOTES_DELETE_STATE, state)
        val result = database.update(
            DBHelper.NOTES_TABLE,
            contentValues,
            "${DBHelper.NOTES_ID}= ?",
            arrayOf(id.toString())
        )
        database.close()
        return result > 0
    }

    fun editNotes(id: Int, notes: DBNotesModel): Boolean {
        val database = db.writableDatabase
        setContentValues(notes)
        val result = database.update(
            DBHelper.NOTES_TABLE,
            contentValues,
            "${DBHelper.NOTES_ID}= ?",
            arrayOf(id.toString())
        )
        database.close()
        return result > 0


    }


    private fun setContentValues(notes: DBNotesModel) {
        contentValues.clear()
        contentValues.put(DBHelper.NOTES_TITLE, notes.title)
        contentValues.put(DBHelper.NOTES_DETAIL, notes.detail)
        contentValues.put(DBHelper.NOTES_DELETE_STATE, notes.deleteState)
        contentValues.put(DBHelper.NOTES_DATE, notes.date)
    }

    fun getNotesForRecycler(value: String): ArrayList<RecyclerNOtesModel> {
        val database = db.readableDatabase
        val query = "SELECT ${DBHelper.NOTES_ID}, ${DBHelper.NOTES_TITLE}, ${DBHelper.NOTES_DETAIL}" +
                " FROM ${DBHelper.NOTES_TABLE}" +
                " WHERE ${DBHelper.NOTES_DELETE_STATE} = ?"
        cursor = database.rawQuery(query, arrayOf(value))
        val data = getDataForRecycler()
        cursor.close()
        database.close()
        return data
    }


    fun getNotesById(id: Int): DBNotesModel {
        val database = db.readableDatabase
        val query = "SELECT * FROM ${DBHelper.NOTES_TABLE} WHERE ${DBHelper.NOTES_ID}= ?"
        cursor = database.rawQuery(query, arrayOf(id.toString()))
        val data = getData()
        cursor.close()
        database.close()
        return data

    }

    private fun getData(): DBNotesModel {

        val data = DBNotesModel(0, "", "", "", "")

        try {
            if (cursor.moveToFirst()) {
                data.id = cursor.getInt(getIndex(DBHelper.NOTES_ID))
                data.title = cursor.getString(getIndex(DBHelper.NOTES_TITLE))
                data.detail = cursor.getString(getIndex(DBHelper.NOTES_DETAIL))
                data.deleteState = cursor.getString(getIndex(DBHelper.NOTES_DELETE_STATE))
                data.date = cursor.getString(getIndex(DBHelper.NOTES_DATE))
            }

        } catch (_: Exception) {
        }
        return data

    }

    private fun getDataForRecycler(): ArrayList<RecyclerNOtesModel> {
        val data = ArrayList<RecyclerNOtesModel>()
        try {
            if (cursor.moveToFirst())
                do {
                    val id = cursor.getInt(getIndex(DBHelper.NOTES_ID))
                    val title = cursor.getString(getIndex(DBHelper.NOTES_TITLE))
                    val body = cursor.getString(getIndex(DBHelper.NOTES_DETAIL))
                    data.add(RecyclerNOtesModel(id = id,title =title,body=body))
                } while (cursor.moveToNext())
        } catch (_: Exception) {
        }
        return data
    }

    private fun getIndex(name: String) = cursor.getColumnIndex(name)

    fun deleteNotes(id: Int, ): Boolean {
        val database = db.writableDatabase
        val result = database.delete(
            DBHelper.NOTES_TABLE,
            "${DBHelper.NOTES_ID}= ?",
            arrayOf(id.toString())
        )
        database.close()
        return result > 0
    }
}