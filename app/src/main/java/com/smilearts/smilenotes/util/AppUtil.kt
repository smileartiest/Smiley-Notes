package com.smilearts.smilenotes.util

import com.google.gson.Gson
import com.smilearts.smilenotes.model.CheckListModel
import com.smilearts.smilenotes.repository.table.CheckListTable
import org.json.JSONArray
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

public class AppUtil {

    private var calendar: Calendar? = null
    private var simpleDateFormat: SimpleDateFormat? = null
    private var simpleTimeFormat: SimpleDateFormat? = null

    //dd.MM.yyyy HH:mm:ss   = 25.09.2021 19:55:40
    //dd.LLL.yyyy HH:mm:ss  = 25 Sep 2021 19:55:40
    //dd.LLLL.yyyy HH:mm:ss = 25 September 2021 19:55:40
    // System.currentTimeMillis()

    fun getDate() : String {
        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("dd LLL yyyy")
        return simpleDateFormat?.format(calendar?.time).toString()
    }

    fun getTime() : String {
        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("HH:mm:ss")
        return simpleDateFormat?.format(calendar?.time).toString()
    }

    fun getBackupName() : String {
        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("ddLLLyyyy")
        simpleTimeFormat = SimpleDateFormat("HHmmss")
        return "SMN_${simpleDateFormat?.format(calendar?.time)}_${simpleTimeFormat?.format(calendar?.time)}.json"
    }

    fun getModelToString(list: ArrayList<CheckListModel.Points>) : String {
        return Gson().toJson(list)
    }

    private fun getStringToModel(jsonStr: String) : ArrayList<CheckListModel.Points> {
        try {
            var tempArray: ArrayList<CheckListModel.Points> = ArrayList()
            val data = JSONArray(jsonStr)
            if (data.length() != 0) {
                for (i in 0 until data.length()) {
                    val jsonOBJ = data.getJSONObject(i)
                    tempArray.add(CheckListModel.Points(
                        jsonOBJ.getString("point"),
                        jsonOBJ.getBoolean("checkStatus"))
                    )
                }
            }
            return tempArray
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ArrayList()
    }

    fun getCheckTableToModelList(tableList: ArrayList<CheckListTable>) : ArrayList<CheckListModel> {
        var tempList: ArrayList<CheckListModel> = ArrayList()
        if (tableList != null) {
            for (table in tableList) {
                tempList.add(CheckListModel(table.id, table.title, getStringToModel(table.points), table.remainder, table.date, table.time, table.bg, table.priority))
            }
        }
        return tempList
    }

    fun resetCheckList(checkList: ArrayList<CheckListModel.Points>) : ArrayList<CheckListModel.Points> {
        var temp: ArrayList<CheckListModel.Points> = ArrayList()
        for (point in checkList) {
            temp.add(CheckListModel.Points(point.point, false))
        }
        return temp
    }

    companion object {
        const val DB_NAME = "NOTES_DB"
        const val NOTES_MODEL = "NOTES_MODEL"
        const val CHECK_LIST_MODEL = "CHECK_LIST_MODEL"
        const val TYPE = "Type"
        const val regRef = "RegProfile"

        //Page Types
        const val NEW_PAGE = "NewPage"
        const val UPDATE_PAGE = "UpdatePage"

        //Color
        const val DEFAULT = "DEFAULT"
        const val LIGHTTHEME = "LIGHTTHEME"
        const val LIGHTYELLOW = "LIGHTYELLOW"
        const val LIGHTBLUE = "LIGHTBLUE"
        const val LIGHTGREEN = "LIGHTGREEN"
        const val DARKYELLOW = "DARKYELLOW"

        //Order
        const val ACENTING = "ACENTING"
        const val DECENTING = "DECENTING"

        const val nID = "id"
        const val nTitle = "title"
        const val nMessage = "message"
        const val nDate = "date"
        const val nTime = "time"
        const val nBg = "bg"
        const val nPriority = "priority"

        //Chats
        const val chID = "id"
        const val chTitle = "title"
        const val chPoints = "points"
        const val chPoint = "point"
        const val chDate = "date"
        const val chTime = "time"
        const val chReminder = "reminder"
        const val chBg = "bg"
        const val chPriority = "priority"

        //Backup
        const val bDate = "date"
        const val bTime = "time"
        const val bNotes = "notes"
        const val bChats = "chats"

        //Notes Type
        const val ALL_NOTES = "All"
        const val SHORT_NOTES = "Shorting"

        //Notes Type
        const val ALL_CHECK = "All"
        const val SHORT_CHECK = "Shorting"

        //Short Type
        const val SHORT_PRI = "ShortPriority"
        const val SHORT_COLOR = "ShortColor"
        const val SHORT_ACENTING ="ShortACE"

        //Main Menu Type
        const val MAIN_MENU_ADD_NOTES = "AddNotes"
        const val MAIN_MENU_CHECK_LIST = "AddCheckList"
        const val MAIN_MENU_BIN = "BinScreen"
        const val MAIN_MENU_SETTINGS = "SettingsScreen"

    }

}