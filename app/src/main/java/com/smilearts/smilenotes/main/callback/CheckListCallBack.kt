package com.smilearts.smilenotes.main.callback

import com.smilearts.smilenotes.model.CheckListModel

interface CheckListCallBack {
    fun optionMenu(checkList: CheckListModel)
    fun moveNextPage(checkList: CheckListModel)
    fun setChooseList(checkList: CheckListModel)
}