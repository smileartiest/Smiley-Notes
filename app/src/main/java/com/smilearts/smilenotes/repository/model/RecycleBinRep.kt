package com.smilearts.smilenotes.repository.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smilearts.smilenotes.model.NotesModel
import com.smilearts.smilenotes.model.RecycleModel
import com.smilearts.smilenotes.repository.RepositoryUtil
import com.smilearts.smilenotes.util.AppUtil

class RecycleBinRep(val util: RepositoryUtil) {

    private val notesLiveData: MutableLiveData<List<RecycleModel>> = MutableLiveData()

    private fun loadNotes() {
        val temp = util.roomDB?.notesDao()?.recycleList
        if (temp != null) {
            if (temp.size != 0) {
                notesLiveData.postValue(temp)
            } else {
                notesLiveData.postValue(null)
            }
        } else {
            notesLiveData.postValue(null)
        }
    }

    fun getBinList() : LiveData<List<RecycleModel>> {
        loadNotes()
        return notesLiveData
    }

    fun removeRecycleBin(model: RecycleModel) {
        util.roomDB?.notesDao()?.DeleteRecycle(model.id)
        util.errorStatus.postValue("Delete Successful")
        loadNotes()
    }

    fun moveAllNotesToRecycleBin() {
        val chooseList = util.roomDB.notesDao().notes
        if (chooseList.size != 0) {
            for (cModel in chooseList) {
                val model = RecycleModel()
                model.title = cModel.title
                model.message = cModel.message
                model.date = AppUtil().getDate()
                util.roomDB?.notesDao()?.InsertRecycle(model)
                util.roomDB?.notesDao()?.DeleteRecycle(cModel.id)
            }
            loadNotes()
            util.errorStatus.postValue("Move Recycle Bin Successful")
        } else {
            util.errorStatus.postValue("No Data Found")
        }
    }

    fun insertNotes(model: RecycleModel) {
        val temp = NotesModel(
            0,
            model.title,
            model.message,
            AppUtil().getDate(),
            AppUtil().getTime(),
            AppUtil.DEFAULT,
            0
        )
        util.roomDB?.notesDao()?.Insert(temp)
        util.roomDB?.notesDao()?.DeleteRecycle(model.id)
        util.errorStatus.postValue("Restore Successful")
        loadNotes()
    }

    fun emptyRecycleBin() {
        util.roomDB.notesDao().DeleteAllRecycle()
        util.errorStatus.postValue("Clear all notes form Bin")
        loadNotes()
    }

}