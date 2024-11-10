package com.example.lab7

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab7.Model.AddNoteActivity
import com.example.lab7.Model.Note
import com.example.lab7.Model.NoteAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private val notes = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notesRecyclerView = findViewById(R.id.notesRecyclerView)
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(notes)
        notesRecyclerView.adapter = noteAdapter

        val addNoteButton: Button = findViewById(R.id.addNoteButton)
        addNoteButton.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }

        loadNotesFromFirebase()
    }

    private fun loadNotesFromFirebase() {
        val database = FirebaseDatabase.getInstance()
        val notesRef = database.getReference("Notes")

        notesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notes.clear()
                for (noteSnapshot in snapshot.children) {
                    val note = noteSnapshot.getValue(Note::class.java)
                    if (note != null) {
                        notes.add(note)
                    }
                }
                Log.d("MainActivity", "Notes loaded: ${notes.size}")
                noteAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show()
            }
        })

    }
}


