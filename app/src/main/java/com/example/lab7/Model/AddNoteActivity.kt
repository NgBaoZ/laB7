package com.example.lab7.Model

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lab7.R
import com.google.firebase.database.FirebaseDatabase

class AddNoteActivity : AppCompatActivity() {
    private lateinit var noteTitle: EditText
    private lateinit var noteContent: EditText
    private lateinit var saveNoteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        noteTitle = findViewById(R.id.noteTitle)
        noteContent = findViewById(R.id.noteContent)
        saveNoteButton = findViewById(R.id.saveNoteButton)

        saveNoteButton.setOnClickListener {
            saveNoteToFirebase()
        }
    }

    private fun saveNoteToFirebase() {
        val title = noteTitle.text.toString()
        val content = noteContent.text.toString()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Điền đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        val database = FirebaseDatabase.getInstance()
        val notesRef = database.getReference("Notes")

        val noteId = notesRef.push().key
        if (noteId == null) {
            Toast.makeText(this, "Lỗi tạo ID cho ghi chú", Toast.LENGTH_SHORT).show()
            return
        }

        val note = Note(noteId, title, content)
        notesRef.child(noteId).setValue(note)
            .addOnSuccessListener {
                Toast.makeText(this, "Đã lưu ghi chú", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 2000) // Đợi 2 giây trước khi đóng activity
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Lỗi lưu ghi chú", Toast.LENGTH_SHORT).show()
                Log.e("AddNoteActivity", "Error saving note", e)
            }
    }
}


