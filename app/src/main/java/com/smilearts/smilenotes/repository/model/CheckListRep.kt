package com.smilearts.smilenotes.repository.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.smilearts.smilenotes.model.CheckListModel
import com.smilearts.smilenotes.repository.RepositoryUtil
import com.smilearts.smilenotes.repository.table.CheckListTable
import com.smilearts.smilenotes.util.AppUtil

class CheckListRep(val util: RepositoryUtil) {

    private val checkListLiveData: MutableLiveData<List<CheckListModel>> = MutableLiveData()

    private fun loadCheckList() {
        val temp = util.roomDB?.checkListDao()?.list
        if (temp != null) {
            if (temp.size != 0) {
                val checkList = AppUtil().getCheckTableToModelList(temp as ArrayList<CheckListTable>)
                checkListLiveData.postValue(checkList)
            } else {
                checkListLiveData.postValue(null)
            }
        } else {
            checkListLiveData.postValue(null)
        }
    }

    fun shortPriority(pri: Int) : LiveData<List<CheckListModel>> {
        Transformations.map(checkListLiveData) {
            if (it == null) {
                checkListLiveData.postValue(null)
                return@map
            } else {
                val fData = it.filter { notesModel ->
                    notesModel.priority == pri
                }
                if (fData.isEmpty()) {
                    checkListLiveData.postValue(null)
                } else {
                    checkListLiveData.postValue(fData)
                }
            }
        }
        return Transformations.map(checkListLiveData) {
            if (it == null) return@map null
            it.filter { checkModel ->
                checkModel.priority == pri
            }
        }
    }

    fun shortBasedColor(color: String) : LiveData<List<CheckListModel>> {
        Transformations.map(checkListLiveData) {
            if (it == null) {
                checkListLiveData.postValue(null)
                return@map
            } else {
                val fData = it.filter { notesModel ->
                    notesModel.bg.contains(color)
                }
                if (fData.isEmpty()) {
                    checkListLiveData.postValue(null)
                } else {
                    checkListLiveData.postValue(fData)
                }
            }
        }
        return Transformations.map(checkListLiveData) {
            if (it == null) return@map null
            it.filter { checkModel ->
                checkModel.bg.contains(color)
            }
        }
    }

    fun shortBasedOrder(type: String): LiveData<List<CheckListModel>> {
        when (type) {
            AppUtil.ACENTING -> {
                val temp = util.roomDB?.checkListDao()?.notesASC
                checkListLiveData?.postValue(AppUtil().getCheckTableToModelList(temp!! as ArrayList<CheckListTable>))
            }
            AppUtil.DECENTING -> {
                val temp = util.roomDB?.notesDao()?.notesDESC
                checkListLiveData?.postValue(AppUtil().getCheckTableToModelList(temp!! as ArrayList<CheckListTable>))
            }
        }
        return checkListLiveData
    }

    fun getAllCheckList() : LiveData<List<CheckListModel>> {
        loadCheckList()
        return checkListLiveData
    }

    fun remove(model: CheckListModel) {
        util.roomDB?.checkListDao()?.Delete(model.checkID)
        util.errorStatus.postValue("Delete Successful")
        loadCheckList()
    }

    fun update(model: CheckListModel) {
        util.roomDB?.checkListDao()?.Update(model.checkID,model.title,AppUtil().getModelToString(model.point), model.reminder,model.date,model.time,model.bg,model.priority)
        util.errorStatus.postValue("Update Successful")
        loadCheckList()
    }

    fun insert(model: CheckListModel) {
        util.roomDB?.checkListDao()?.Insert(CheckListTable(model.checkID,model.title,AppUtil().getModelToString(model.point), model.reminder,model.date,model.time,model.bg,model.priority))
        util.errorStatus.postValue("Save Successful")
        loadCheckList()
    }

    fun clearAllCheckList() {
        util.roomDB.checkListDao().ClearAll()
        util.errorStatus.postValue("Clear all notes form check list")
        loadCheckList()
    }

}