package Dao

import Model.Note
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): List<Note>

    @Delete
    fun delete(note: Note)
    @Update
    fun update(note: Note)
}
