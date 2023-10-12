package com.smilearts.smilenotes.main.callback

import com.smilearts.smilenotes.model.NotesModel

interface NotesCallBack {
    fun setChooseList(model: NotesModel?)
    fun notesOptionScreen(model: NotesModel?)
    fun moveToNotesPage(model: NotesModel?)
}