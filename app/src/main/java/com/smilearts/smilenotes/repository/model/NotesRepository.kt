package com.smilearts.smilenotes.repository.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.smilearts.smilenotes.model.NotesModel
import com.smilearts.smilenotes.model.RecycleModel
import com.smilearts.smilenotes.repository.RepositoryUtil
import com.smilearts.smilenotes.repository.table.CheckListTable
import com.smilearts.smilenotes.model.CheckListModel
import com.smilearts.smilenotes.util.AppUtil
import org.json.JSONObject

class NotesRepository(val util: RepositoryUtil) {

    private val notesLiveData: MutableLiveData<List<NotesModel>> = MutableLiveData()

    private val tempNotesList: MutableLiveData<List<NotesModel>> = MutableLiveData()

    var chooseList: ArrayList<NotesModel> = ArrayList()
    var chooseStatus: Boolean = false
    val chooseMenu: MutableLiveData<Boolean> = MutableLiveData()

    fun loadNotes() {
        val data = util.roomDB?.notesDao()!!.notes
        if (data.isNotEmpty()) {
            notesLiveData?.postValue(data)
        } else {
            notesLiveData.postValue(null)
        }
    }

    fun getAllNotes() : LiveData<List<NotesModel>> {
        loadNotes()
        return notesLiveData
    }

    fun getNoteWidgetID(int: Int) : LiveData<List<NotesModel>> {
        loadNotes()
        return Transformations.map(notesLiveData) {
            it.filter { notesModel ->
                notesModel.id == int
            }
        }
    }

    fun shortPriority(pri: Int) : LiveData<List<NotesModel>> {
        Transformations.map(notesLiveData) {
            if (it == null) {
                notesLiveData.postValue(null)
                return@map
            } else {
                val fData = it.filter { notesModel ->
                    notesModel.priority == pri
                }
                if (fData.isEmpty()) {
                    notesLiveData.postValue(null)
                } else {
                    notesLiveData.postValue(fData)
                }
            }
        }
        return Transformations.map(notesLiveData) {
            if (it == null) return@map null
            it.filter { notesModel ->
                notesModel.priority == pri
            }
        }
    }

    fun shortBasedColor(color: String) : LiveData<List<NotesModel>> {
        Transformations.map(notesLiveData) {
            if (it == null) {
                notesLiveData.postValue(null)
                return@map
            } else {
                val fData = it.filter { notesModel ->
                    notesModel.bg.contains(color)
                }
                if (fData.isEmpty()) {
                    notesLiveData.postValue(null)
                } else {
                    notesLiveData.postValue(fData)
                }
            }
        }
        return Transformations.map(notesLiveData) {
            if (it == null) return@map null
            it.filter { notesModel ->
                notesModel.bg.contains(color)
            }
        }
    }

    fun shortBasedOrder(type: String): LiveData<List<NotesModel>> {
        when (type) {
            AppUtil.ACENTING -> {
                val temp = util.roomDB?.notesDao()?.notesASC
                tempNotesList?.postValue(temp!!)
            }
            AppUtil.DECENTING -> {
                val temp = util.roomDB?.notesDao()?.notesDESC
                tempNotesList?.postValue(temp!!)
            }
        }
        return tempNotesList
    }

    fun insertNote(model: NotesModel) {
        util.roomDB?.notesDao()?.Insert(model)
        loadNotes()
        util.errorStatus.postValue("Insert Successful")
    }

    fun delete(id: Int) {
        util.roomDB?.notesDao()?.DeleteNote(id)
        loadNotes()
        util.errorStatus.postValue("Delete Successful")
    }

    fun update(model: NotesModel) {
        util.roomDB?.notesDao()?.Update(
            model.id,
            model.title,
            model.message,
            model.time,
            model.date,
            model.bg,
            model.priority
        )
        loadNotes()
        util.errorStatus.postValue("Update Successful")
    }

    fun setChooseList(model: NotesModel) {
        if (chooseList.contains(model)) {
            cancelChoice(model)
        } else {
            choose(model)
        }
    }

    private fun cancelChoice(model: NotesModel) {
        if (chooseList.size == 1) {
            chooseList.remove(model)
            chooseStatus = false
            chooseMenu.postValue(false)
        } else {
            chooseList.remove(model)
        }
    }

    private fun choose(model: NotesModel) {
        if (chooseStatus) {
            chooseList.add(model)
        } else {
            chooseStatus = true
            chooseList.add(model)
            chooseMenu.postValue(true)
        }
    }

    fun cancelAll() {
        if (chooseList != null && chooseList.size != 0) chooseList.clear()
        chooseStatus = false
        loadNotes()
    }

    fun deleteAll() {
        if (chooseList.isNotEmpty()) {
            for (model in chooseList) {
                util.roomDB?.notesDao()?.DeleteNote(model.id)
            }
            chooseStatus = false
            loadNotes()
            chooseList = arrayListOf()
            chooseMenu.postValue(false)
            util.errorStatus.postValue("Delete Successful")
        } else {
            util.roomDB.notesDao().ClearNotes()
            chooseStatus = false
            loadNotes()
            chooseList = arrayListOf()
            chooseMenu.postValue(false)
            util.errorStatus.postValue("Delete Successful")
        }
    }

    fun moveBin() {
        if (chooseList.size != 0) {
            for (cModel in chooseList) {
                val model = RecycleModel()
                model.title = cModel.title
                model.message = cModel.message
                model.date = AppUtil().getDate()
                util.roomDB?.notesDao()?.InsertRecycle(model)
                util.roomDB?.notesDao()?.DeleteNote(cModel.id)
            }
            loadNotes()
            chooseMenu.postValue(false)
            chooseList = arrayListOf()
            util.errorStatus.postValue("Move Recycle bin successful")
        }
    }

    fun moveBin(notesModel: NotesModel) {
        var model = RecycleModel()
        model.title = notesModel.title
        model.message = notesModel.message
        model.date = AppUtil().getDate()
        util.roomDB?.notesDao()?.InsertRecycle(model)
        util.roomDB?.notesDao()?.DeleteNote(notesModel.id)
        loadNotes()
        util.errorStatus.postValue("Move Recycle bin successful")
    }

    fun getBackupAllNotes() : String? {
        var data = JsonObject()
        try {
            val notesList = util.roomDB.notesDao().notes
            val chatList = AppUtil().getCheckTableToModelList(util.roomDB.checkListDao().list as ArrayList<CheckListTable>)
            var tempNotesData = JsonArray()
            var tempChatsData = JsonArray()
            for (note in notesList) {
                var obj = JsonObject()
                obj.addProperty(AppUtil.nID , note.id)
                obj.addProperty(AppUtil.nTitle , note.title)
                obj.addProperty(AppUtil.nMessage , note.message)
                tempNotesData.add(obj)
            }
            for (chat in chatList) {
                var obj = JsonObject()
                var points = JsonArray()
                obj.addProperty(AppUtil.chID, chat.checkID)
                obj.addProperty(AppUtil.chTitle, chat.title)
                for (point in chat.point) {
                    val obj = JsonObject()
                    obj.addProperty(AppUtil.chPoint, point.point)
                    points.add(obj)
                }
                obj.add(AppUtil.chPoints, points)
                tempChatsData.add(obj)
            }
            if (!tempNotesData.isEmpty) {
                data.addProperty(AppUtil.nDate,AppUtil().getDate())
                data.addProperty(AppUtil.nTime,AppUtil().getTime())
                data.add(AppUtil.bNotes,tempNotesData)
                data.add(AppUtil.bChats,tempChatsData)
                return data.toString()
            } else {
                util.errorStatus.postValue("No data found")
            }
        } catch (e: Exception) {
            Log.d("", "Exception: ${e.localizedMessage}")
            util.errorStatus.postValue(e.localizedMessage);
        }
        return null
    }

    fun restoreBackup(strData: String) {
        try {
            val jsonData = JSONObject(strData)
            val jsonArray = jsonData.getJSONArray(AppUtil.bNotes)
            val currentNotes = util.roomDB.notesDao().notes
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                var store = true
                if (currentNotes.size != 0) {
                    for (cNote in currentNotes) {
                        if (cNote.title == obj.getString(AppUtil.nTitle) && cNote.message == obj.getString(AppUtil.nMessage)) {
                            store = false
                            break
                        }
                    }
                }
                if (store) {
                    val tempData = NotesModel()
                    tempData.title = obj.getString(AppUtil.nTitle)
                    tempData.message = obj.getString(AppUtil.nMessage)
                    tempData.bg = AppUtil.DEFAULT
                    tempData.date = AppUtil().getDate()
                    tempData.time = AppUtil().getTime()
                    tempData.priority = 0
                    insertNote(tempData)
                }
            }
            if (jsonData.has(AppUtil.bChats)) {
                val chatsArray = jsonData.getJSONArray(AppUtil.bChats)
                val currentChats = util.roomDB.checkListDao().list
                for (i in 0 until chatsArray.length()) {
                    val obj = chatsArray.getJSONObject(i)
                    var store = true
                    if (currentChats.size != 0) {
                        for (chat in currentChats) {
                            if (chat.title == obj.getString(AppUtil.chTitle)) {
                                store = false
                                break
                            }
                        }
                    }
                    if (store) {
                        val checkModel = CheckListTable()
                        checkModel.title = obj.getString(AppUtil.chTitle)
                        var tempPoints: ArrayList<CheckListModel.Points> = ArrayList()
                        val pointsArray = obj.getJSONArray(AppUtil.chPoints)
                        for (i in 0 until pointsArray.length()) {
                            val pointObj = pointsArray.getJSONObject(i)
                            tempPoints.add(CheckListModel.Points(pointObj.getString(AppUtil.chPoint), false))
                        }
                        checkModel.points = AppUtil().getModelToString(tempPoints)
                        checkModel.remainder = ""
                        checkModel.date = AppUtil().getDate()
                        checkModel.time = AppUtil().getTime()
                        checkModel.bg = AppUtil.DEFAULT
                        checkModel.priority = 0
                        util.roomDB.checkListDao().Insert(checkModel)
                    }
                }
            }
            if (jsonArray.length() != 0) {
                util.errorStatus.postValue("Restore Success")
            } else {
                util.errorStatus.postValue("No Data Found")
            }
        } catch (e: Exception) {
            util.errorStatus.postValue(e.message)
        }
    }

}