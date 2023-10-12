package com.smilearts.smilenotes.main.callback

interface FilterCallBack {
    fun shortPriority(priority: Int)
    fun shortColor(color: String?)
    fun shortACDC(acdc: String?)
    fun allNotes()
}