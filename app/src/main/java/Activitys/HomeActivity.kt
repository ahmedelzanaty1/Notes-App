package Activitys

import DataBase.NoteDatabase
import Model.Note
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.notesapplication.Adapter.NotesAdapter
import com.example.notesapplication.R
import com.example.notesapplication.databinding.ActivityHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {
lateinit var binding: ActivityHomeBinding
lateinit var adapter: NotesAdapter
    private var notesList: MutableList<Note> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.extendedFab.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
            binding.img.visibility = View.GONE
        }
        adapter = NotesAdapter(notesList)
        binding.recyclerView.adapter = adapter
        fetchNotes()
    }
    override fun onResume() {
        super.onResume()
        fetchNotes()
    }
    private fun fetchNotes() {
        lifecycleScope.launch {
            try {
                val notes = withContext(Dispatchers.IO) {
                    NoteDatabase.getDatabase(this@HomeActivity).noteDao().getAllNotes()
                }

                notesList.clear()
                notesList.addAll(notes)
                adapter.notifyDataSetChanged()

                binding.img.visibility = if (notesList.isEmpty()) View.VISIBLE else View.GONE
                binding.textId.visibility = if (notesList.isEmpty()) View.VISIBLE else View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}