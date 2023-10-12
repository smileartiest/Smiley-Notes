package com.smilearts.smilenotes.main.baseclass

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.smilearts.smilenotes.main.checklistpage.viewmodel.CheckListPageViewModel
import com.smilearts.smilenotes.main.mainpage.viewmodel.MainPageViewModel
import com.smilearts.smilenotes.main.notespage.viewmodel.NotesPageViewModel
import com.smilearts.smilenotes.repository.RepositoryUtil

class BaseClass(
    private val context: Context,
    private val repositoryUtil: RepositoryUtil
) : NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            MainPageViewModel::class.java -> {
                MainPageViewModel(context, repositoryUtil) as T
            }
            NotesPageViewModel::class.java -> {
                NotesPageViewModel(context, repositoryUtil) as T
            }
            CheckListPageViewModel::class.java -> {
                CheckListPageViewModel(context, repositoryUtil) as T
            }
            else -> super.create(modelClass)
        }
    }

}