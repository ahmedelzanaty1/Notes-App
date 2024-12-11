package com.example.notesapplication.Adapter

import Model.Note

interface OnNoteClick {
    fun onnoteclick(note: Note , position: Int)
}