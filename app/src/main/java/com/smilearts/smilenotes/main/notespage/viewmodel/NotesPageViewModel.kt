package com.smilearts.smilenotes.main.notespage.viewmodel

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.smilearts.smilenotes.R
import com.smilearts.smilenotes.main.notespage.screens.NotesEditPage
import com.smilearts.smilenotes.model.NotesModel
import com.smilearts.smilenotes.repository.RepositoryUtil
import com.smilearts.smilenotes.util.AppUtil

class NotesPageViewModel(
    private val context: Context,
    private val repositoryUtil: RepositoryUtil
    ) : ViewModel() {
    var notesModel: NotesModel? = NotesModel()
    private var pageType: String? = ""
    var updateStatus = false
        private set
    var resumeScreen = false
    var liveNotesModel = MutableLiveData<NotesModel?>()
    var screenCloseStatus = MutableLiveData<Boolean>()

    init {
        screenCloseStatus.postValue(false)
    }

    fun notesRefresh() {
        if (notesModel!!.title != null || notesModel!!.message != null) liveNotesModel.postValue(
            notesModel
        )
    }

    private fun setStoreStatus(sts: Boolean) {
        updateStatus = sts
    }

    fun setType(intent: Intent) {
        pageType = intent.extras!!.getString(AppUtil.TYPE)
        if (pageType == AppUtil.UPDATE_PAGE) {
            notesModel = intent.getSerializableExtra(AppUtil.NOTES_MODEL) as NotesModel?
            liveNotesModel.postValue(notesModel)
        }
    }

    fun setTitle(tit: String?) {
        if (!notesModel!!.title.equals(tit, ignoreCase = true)) setStoreStatus(true)
        notesModel!!.title = tit
    }

    fun setMessage(msg: String?) {
        if (!notesModel!!.message.equals(msg, ignoreCase = true)) setStoreStatus(true)
        notesModel!!.message = msg
    }

    fun setBG(bg: String?) {
        if (!notesModel!!.bg.equals(bg, ignoreCase = true)) setStoreStatus(true)
        notesModel!!.bg = bg
        liveNotesModel.postValue(notesModel)
    }

    fun setPriority(pri: Int) {
        if (notesModel!!.priority != pri) setStoreStatus(true)
        notesModel!!.priority = pri
        liveNotesModel.postValue(notesModel)
    }

    fun backPress() {
        if (pageType.equals(AppUtil.NEW_PAGE, ignoreCase = true)) {
            if (notesModel!!.title != null || notesModel!!.message != null) {
                insertNotes()
            }
        } else {
            updateNotes()
        }
    }

    private fun updateNotes() {
        notesModel!!.date = AppUtil().getDate()
        notesModel!!.time = AppUtil().getTime()
        repositoryUtil.notesRep.update(notesModel!!)
        setStoreStatus(false)
        screenCloseStatus.postValue(true)
    }

    private fun insertNotes() {
        notesModel!!.bg = AppUtil.DEFAULT
        notesModel!!.date = AppUtil().getDate()
        notesModel!!.time = AppUtil().getTime()
        repositoryUtil.notesRep.insertNote(notesModel!!)
        setStoreStatus(false)
        screenCloseStatus.postValue(true)
    }

    fun moveBin() {
        repositoryUtil.notesRep.moveBin(notesModel!!)
        screenCloseStatus.postValue(true)
    }

    fun delete() {
        repositoryUtil.notesRep.delete(notesModel!!.id)
        screenCloseStatus.postValue(true)
    }

    fun share() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT,"Title : ${notesModel!!.title} \nNotes : \n${notesModel!!.message}")
        sendIntent.type = "text/plain"
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun notesOptionScreen(model: NotesModel) {
        notesModel = model
        val sheet = BottomSheetDialog(context)
        sheet.setContentView(R.layout.bottom_sheet_notes_options)
        val priority = sheet.findViewById<ImageView>(R.id.bt_sheet_priority_icon)
        if (model.priority == 0) {
            priority!!.setImageResource(R.drawable.non_priority_icon)
        } else {
            priority!!.setImageResource(R.drawable.priority_icon)
        }

        priority.setOnClickListener {
            if (model.priority == 0) {
                notesModel!!.priority = 1
            } else {
                notesModel!!.priority = 0
            }
            repositoryUtil.notesRep.update(notesModel!!)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bt_sheet_default_bg_icon)!!.setOnClickListener {
            notesModel!!.bg = AppUtil.DEFAULT
            repositoryUtil.notesRep.update(notesModel!!)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bt_sheet_lighttheme_bg_icon)!!.setOnClickListener {
            notesModel!!.bg = AppUtil.LIGHTTHEME
            repositoryUtil.notesRep.update(notesModel!!)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bt_sheet_lightblue_bg_icon)!!.setOnClickListener {
            notesModel!!.bg = AppUtil.LIGHTBLUE
            repositoryUtil.notesRep.update(notesModel!!)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bt_sheet_lightgreen_bg_icon)!!.setOnClickListener {
            notesModel!!.bg = AppUtil.LIGHTGREEN
            repositoryUtil.notesRep.update(notesModel!!)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bt_sheet_lightyellow_bg_icon)!!.setOnClickListener {
            notesModel!!.bg = AppUtil.LIGHTYELLOW
            repositoryUtil.notesRep.update(notesModel!!)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bt_sheet_darkyellow_bg_icon)!!.setOnClickListener {
            notesModel!!.bg = AppUtil.DARKYELLOW
            repositoryUtil.notesRep.update(notesModel!!)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bt_sheet_delete)!!.setOnClickListener {
            repositoryUtil.notesRep.delete(notesModel!!.id)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bt_sheet_bin)!!.setOnClickListener {
            repositoryUtil.notesRep.moveBin(notesModel!!)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bt_sheet_share)!!.setOnClickListener {
            share()
            sheet.dismiss()
        }

        sheet.show()
        sheet.dismissWithAnimation = true
        sheet.setOnDismissListener { dialog: DialogInterface -> dialog.dismiss() }
    }

    fun notesScreen() {
        context.startActivity(
            Intent(context, NotesEditPage::class.java)
                .putExtra(AppUtil.TYPE, AppUtil.NEW_PAGE)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

}