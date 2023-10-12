package com.smilearts.smilenotes.main.mainpage.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.smilearts.smilenotes.main.notespage.LandingPageNotes
import com.smilearts.smilenotes.main.recyclebinpage.RecycleBin
import com.smilearts.smilenotes.main.SettingPage
import com.smilearts.smilenotes.main.checklistpage.LandingPageCheckList
import com.smilearts.smilenotes.main.checklistpage.screens.EditCheckListPage
import com.smilearts.smilenotes.main.notespage.screens.NotesEditPage
import com.smilearts.smilenotes.repository.RepositoryUtil
import com.smilearts.smilenotes.util.AppUtil

class MainPageViewModel(
    var context: Context,
    var repositoryUtil: RepositoryUtil
    ) : ViewModel() {

    var TAG = "Main Page"

    fun binScreen() {
        context.startActivity(Intent(context, RecycleBin::class.java))
    }

    fun notesScreen() {
        context.startActivity(
            Intent(context, LandingPageNotes::class.java)
                .putExtra(AppUtil.TYPE, AppUtil.NEW_PAGE)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    fun addNotesScreen() {
        context.startActivity(
            Intent(context, NotesEditPage::class.java)
                .putExtra(AppUtil.TYPE, AppUtil.NEW_PAGE)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    fun addCheckListScreen() {
        context.startActivity(
            Intent(context, EditCheckListPage::class.java)
                .putExtra(AppUtil.TYPE, AppUtil.NEW_PAGE)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    fun checkListScreen() {
        context.startActivity(
            Intent(context, LandingPageCheckList::class.java)
                .putExtra(AppUtil.TYPE, AppUtil.NEW_PAGE)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    fun settingScreen() {
        context.startActivity(Intent(context, SettingPage::class.java))
    }

}