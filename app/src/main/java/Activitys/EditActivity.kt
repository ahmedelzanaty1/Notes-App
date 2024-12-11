package Activitys

import DataBase.NoteDatabase
import Model.Note
import android.app.ProgressDialog.show
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notesapplication.R
import com.example.notesapplication.databinding.ActivityEditBinding
import kotlin.properties.Delegates

class EditActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditBinding
    lateinit var title : String
    lateinit var id : String
    lateinit var description : String
    lateinit var colors : List<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backIc.setOnClickListener {
            finish()
        }
        title = intent.getStringExtra("note").toString()
        description = intent.getStringExtra("description").toString()
        id = intent.getIntExtra("id", 0).toString()
        binding.titleEt.setText(title)
        binding.descriptionEt.setText(description)
        binding.saveIc.setOnClickListener {
            if (validateFields()) {
               AlertDialog.Builder(this).setTitle("Update Note")
                   .setMessage("Are you sure you want to update this note?")
                   .setPositiveButton("Yes") { _, _ ->
                       updateNote()
                   }
                   .setNegativeButton("No") { _, _ ->
                       finish()

                   }.show()
        }
        }

    }

    private fun updateNote() {
        val title = binding.titleEt.text.toString()
        val description = binding.descriptionEt.text.toString()
        val id = intent.getIntExtra("id", 0)
        colors = listOf(R.color.color1, R.color.color2, R.color.color3, R.color.color4, R.color.color5, R.color.color6)
        val colorres = colors.random()
        val color = ContextCompat.getColor(this, colorres)
        val note = Note(id = id, title = title, description = description, color = color)
        NoteDatabase.getDatabase(this@EditActivity).noteDao().update(note)
        finish()

    }

    private fun validateFields(): Boolean {
        val title = binding.titleEt.text.toString()
        if (title.isEmpty()) {
            binding.titleEt.error = "Title is required"
            return false
        } else {
            return true
        }

    }
}