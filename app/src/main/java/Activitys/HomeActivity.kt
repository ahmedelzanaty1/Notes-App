package Activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notesapplication.Adapter.NotesAdapter
import com.example.notesapplication.R
import com.example.notesapplication.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
lateinit var binding: ActivityHomeBinding
lateinit var adapter: NotesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.extendedFab.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
            binding.imagePic.visibility = View.GONE
        }

    }
}