package Activitys

import DataBase.NoteDatabase
import Model.Note
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.notesapplication.Colors
import com.example.notesapplication.databinding.ActivityAddNoteBinding
import kotlinx.coroutines.launch

class AddNoteActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backIc.setOnClickListener {
            finish()
        }
        saveNote()
    }

    private fun saveNote() {
        val color = Colors.colors.random()
        binding.saveIc.setOnClickListener {
            if (validateFields()) {
                val title = binding.titleEt.text.toString()
                val description = binding.descriptionEt.text.toString()
                val note = Note(title = title, description = description, color = color)
                lifecycleScope.launch {
                    NoteDatabase.getDatabase(this@AddNoteActivity).noteDao().insert(note)
                    finish()
                }
            }
        }
    }
    private fun validateFields(): Boolean {
        val title = binding.titleEt.text.toString()
        if (title.isEmpty()) {
            binding.titleEt.error = "Title is required"
            return false
        } else {
            binding.titleEt.error = null
        }
        return true
    }
}
