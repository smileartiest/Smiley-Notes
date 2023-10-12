package com.smilearts.smilenotes.main.checklistpage.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.smilearts.smilenotes.databinding.ActivityEditCheckListPageBinding
import com.smilearts.smilenotes.main.checklistpage.screens.adapter.AddCheckListAdapter
import com.smilearts.smilenotes.model.CheckListModel
import com.smilearts.smilenotes.repository.RepositoryUtil
import com.smilearts.smilenotes.util.AppUtil
import com.smilearts.smilenotes.util.TempData

class EditCheckListPage : AppCompatActivity() {

    private lateinit var binding: ActivityEditCheckListPageBinding
    private lateinit var repositoryUtil: RepositoryUtil
    private lateinit var checkAdapter: AddCheckListAdapter
    private lateinit var checkListModel: CheckListModel
    private var pageType: String? = ""
    private var checkList: ArrayList<CheckListModel.Points> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCheckListPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repositoryUtil = RepositoryUtil(this, TempData(applicationContext))
        checkListModel = CheckListModel()
        checkList = arrayListOf()

        if (intent.extras != null) {
            pageType = intent.extras!!.getString(AppUtil.TYPE)
            if (pageType == AppUtil.UPDATE_PAGE) {
                checkListModel = intent.getSerializableExtra(AppUtil.CHECK_LIST_MODEL) as CheckListModel
                binding.editCheckListAddTitle.setText(checkListModel.title)
                checkList = checkListModel.point
                binding.editCheckListSaveTitle.text = "Update"
            } else {
                binding.editCheckListSaveTitle.text = "Save"
            }
        } else {
            binding.editCheckListSaveTitle.text = "Save"
        }

        checkAdapter = AddCheckListAdapter(checkList, object : CheckListCallBack {
            override fun checked(model: CheckListModel.Points, position: Int) {
                model.checkStatus = true
                updateList(model, add = false, update = true, updateLocation = position)
                checkAdapter.updateList(checkList)
            }

            override fun remove(model: CheckListModel.Points) {
                updateList(model, add = false, update = false, updateLocation = 0)
                checkAdapter.updateList(checkList)
            }
        }, false)
        binding.editCheckListListView.setHasFixedSize(true)
        binding.editCheckListListView.adapter = checkAdapter

    }

    override fun onResume() {
        super.onResume()

        binding.editCheckListAddTitle.requestFocus()

        binding.editCheckListAdd.setOnClickListener {
            val point = binding.editCheckListAddPoints.text
            if (point.toString().isNotEmpty()) {
                var model = CheckListModel.Points()
                model.point = point.toString()
                model.checkStatus = false
                updateList(model, add = true, update = false, updateLocation = 0)
                checkAdapter.updateList(checkList)
                checkListModel.point = checkList
                binding.editCheckListAddPoints.setText("")
                showStatus("Point added")
            } else {
                showStatus("Please fill point")
            }
        }

        binding.editCheckListSetRemainder.setOnClickListener {
            showStatus("Coming soon!!!")
        }

        binding.editCheckListSave.setOnClickListener {
            val titleStr = binding.editCheckListAddTitle.text.toString()
            if (titleStr.isNotEmpty()) {
                if (checkList.size > 0) {
                    checkListModel.title = titleStr
                    checkListModel.reminder = ""
                    checkListModel.bg = AppUtil.DEFAULT
                    checkListModel.date = AppUtil().getDate()
                    checkListModel.time = AppUtil().getTime()
                    checkListModel.priority = 0
                    if (pageType == AppUtil.UPDATE_PAGE) {
                        repositoryUtil.checkListRep.update(checkListModel)
                    } else {
                        repositoryUtil.checkListRep.insert(checkListModel)
                    }
                    finish()
                } else {
                    checkListModel.title = titleStr
                    showStatus("Please fill points")
                    binding.editCheckListAddPoints.requestFocus()
                }
            } else {
                showStatus("Please fill title")
                binding.editCheckListAddTitle.requestFocus()
            }
        }

        repositoryUtil.errorStatus.observe(this) {
            if (it != null) showStatus(it)
        }

    }

    private fun updateList(check: CheckListModel.Points, add: Boolean, update: Boolean, updateLocation: Int) {
        if (add) {
            if (!checkList.contains(check)) {
                checkList.add(check)
            }
        } else if (update) {
            checkList.add(updateLocation, check)
        }else {
            checkList.remove(check)
        }
    }

    private fun showStatus(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

}