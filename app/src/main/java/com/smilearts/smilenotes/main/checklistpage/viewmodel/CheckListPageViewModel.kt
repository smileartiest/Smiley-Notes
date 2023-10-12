package com.smilearts.smilenotes.main.checklistpage.viewmodel

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.smilearts.smilenotes.R
import com.smilearts.smilenotes.main.checklistpage.adapter.CheckListActionAdapter
import com.smilearts.smilenotes.main.checklistpage.screens.CheckListCallBack
import com.smilearts.smilenotes.main.checklistpage.screens.EditCheckListPage
import com.smilearts.smilenotes.model.CheckListModel
import com.smilearts.smilenotes.repository.RepositoryUtil
import com.smilearts.smilenotes.util.AppUtil

class CheckListPageViewModel(
    private val context: Context,
    private val repositoryUtil: RepositoryUtil
) : ViewModel() {

    var checkModel: CheckListModel = CheckListModel()
    var checkList: ArrayList<CheckListModel.Points> = ArrayList()
    var adapterCheckList: CheckListActionAdapter? = null

    fun editCheckListScreen() {
        context.startActivity(
            Intent(context, EditCheckListPage::class.java)
                .putExtra(AppUtil.TYPE, AppUtil.NEW_PAGE)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    fun share() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT,"Title : ${checkModel.title} \nPoints : \n${AppUtil().getModelToString(checkModel.point)}")
        sendIntent.type = "text/plain"
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun checkListOptionScreen(model: CheckListModel) {
        checkModel = model
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
                checkModel.priority = 1
            } else {
                checkModel.priority = 0
            }
            repositoryUtil.checkListRep.update(checkModel)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bt_sheet_default_bg_icon)!!.setOnClickListener {
            checkModel.bg = AppUtil.DEFAULT
            repositoryUtil.checkListRep.update(checkModel)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bt_sheet_lighttheme_bg_icon)!!.setOnClickListener {
            checkModel.bg = AppUtil.LIGHTTHEME
            repositoryUtil.checkListRep.update(checkModel)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bt_sheet_lightblue_bg_icon)!!.setOnClickListener {
            checkModel.bg = AppUtil.LIGHTBLUE
            repositoryUtil.checkListRep.update(checkModel)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bt_sheet_lightgreen_bg_icon)!!.setOnClickListener {
            checkModel.bg = AppUtil.LIGHTGREEN
            repositoryUtil.checkListRep.update(checkModel)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bt_sheet_lightyellow_bg_icon)!!.setOnClickListener {
            checkModel.bg = AppUtil.LIGHTYELLOW
            repositoryUtil.checkListRep.update(checkModel)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bt_sheet_darkyellow_bg_icon)!!.setOnClickListener {
            checkModel.bg = AppUtil.DARKYELLOW
            repositoryUtil.checkListRep.update(checkModel)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bt_sheet_delete)!!.setOnClickListener {
            repositoryUtil.checkListRep.remove(checkModel)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bt_sheet_bin)!!.visibility = View.GONE

        sheet.findViewById<View>(R.id.bt_sheet_share)!!.setOnClickListener {
            share()
            sheet.dismiss()
        }

        sheet.show()
        sheet.dismissWithAnimation = true
        sheet.setOnDismissListener { dialog: DialogInterface -> dialog.dismiss() }
    }

    fun checkListDetails(model: CheckListModel) {
        checkModel = model
        checkList = ArrayList()
        checkList = model.point
        val sheet = BottomSheetDialog(context)
        sheet.setContentView(R.layout.bottom_sheet_checklist_action)

        val title: TextView? = sheet.findViewById(R.id.bottom_sheet_checklist_title)
        val listView: RecyclerView? = sheet.findViewById(R.id.bottom_sheet_checklist_list)

        title?.text = model.title

        adapterCheckList = CheckListActionAdapter(checkList, object : CheckListCallBack {
            override fun checked(model: CheckListModel.Points, position: Int) {
                checkList[position] = CheckListModel.Points(model.point, true)
                adapterCheckList?.update(checkList)
                repositoryUtil.checkListRep.update(checkModel)
            }

            override fun remove(model: CheckListModel.Points) {
                //TODO("Not yet implemented")
            }

        })
        listView?.setHasFixedSize(true)
        listView?.adapter = adapterCheckList

        sheet.findViewById<View>(R.id.bottom_sheet_checklist_edit)!!.setOnClickListener {
            val intent = Intent(context, EditCheckListPage::class.java)
            intent.putExtra(AppUtil.TYPE, AppUtil.UPDATE_PAGE)
            intent.putExtra(AppUtil.CHECK_LIST_MODEL, checkModel)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            sheet.dismiss()
        }

        sheet.findViewById<View>(R.id.bottom_sheet_checklist_reset)!!.setOnClickListener {
            checkModel.point = AppUtil().resetCheckList(checkList)
            /**
             * This is for Current screen update
             */
            checkList = checkModel.point
            adapterCheckList?.update(checkModel.point)
            /**
             * ENd
             */
            /**
             * This is for DP update
             */
            repositoryUtil.checkListRep.update(checkModel)
        }

        sheet.show()
        sheet.dismissWithAnimation = true
        sheet.setOnDismissListener { dialog: DialogInterface -> dialog.dismiss() }
    }

}