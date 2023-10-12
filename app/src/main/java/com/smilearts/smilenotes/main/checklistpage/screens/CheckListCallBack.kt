package com.smilearts.smilenotes.main.checklistpage.screens

import com.smilearts.smilenotes.model.CheckListModel

interface CheckListCallBack {
    fun checked(model: CheckListModel.Points, position: Int)
    fun remove(model: CheckListModel.Points)
}