package com.smilearts.smilenotes.main.callback

import com.smilearts.smilenotes.model.BackUpModel
import com.smilearts.smilenotes.model.RecycleModel

interface RecycleCallBack {
    fun chooseNote(model: RecycleModel?)
    fun chooseNote(model: BackUpModel?)
}