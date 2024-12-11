package Activitys

import DataBase.NoteDatabase
import Model.Note
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapplication.Adapter.NotesAdapter
import com.example.notesapplication.Adapter.OnNoteClick
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
        adapter.Onnoteclick = object : OnNoteClick {
            override fun onnoteclick(note: Note, position: Int) {
                val intent = Intent(this@HomeActivity, EditActivity::class.java)
                intent.putExtra("note", note.title)
                intent.putExtra("description", note.description)
                intent.putExtra("id", note.id)
                startActivity(intent)
            }

        }
         swipetodelete()
    }
    private fun swipetodelete() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = notesList[position]
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        NoteDatabase.getDatabase(this@HomeActivity).noteDao().delete(note)
                    }
                    notesList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    binding.img.visibility = if (notesList.isEmpty()) View.VISIBLE else View.GONE
                    binding.textId.visibility = if (notesList.isEmpty()) View.VISIBLE else View.GONE
                }
            }
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                if (dX < 0) {
                    val paint = Paint()
                    paint.color = Color.RED
                    c.drawRect(
                        viewHolder.itemView.right + dX,
                        viewHolder.itemView.top.toFloat(),
                        viewHolder.itemView.right.toFloat(),
                        viewHolder.itemView.bottom.toFloat(),
                        paint
                    )
                    val icon = ContextCompat.getDrawable(this@HomeActivity, R.drawable.ic_delete) // تأكد من إضافة الأيقونة في drawable
                    icon?.setBounds(
                        viewHolder.itemView.right - 150,
                        viewHolder.itemView.top + 20,
                        viewHolder.itemView.right - 100,
                        viewHolder.itemView.bottom - 20
                    )
                    icon?.draw(c)
                }
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
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