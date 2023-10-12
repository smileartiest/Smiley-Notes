package com.smilearts.smilenotes.main.checklistpage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.smilearts.smilenotes.databinding.CheckListLandingPageBinding
import com.smilearts.smilenotes.main.baseclass.BaseClass
import com.smilearts.smilenotes.main.callback.CheckListCallBack
import com.smilearts.smilenotes.main.callback.FilterCallBack
import com.smilearts.smilenotes.main.checklistpage.adapter.CheckListMainAdapter
import com.smilearts.smilenotes.main.checklistpage.screens.EditCheckListPage
import com.smilearts.smilenotes.main.checklistpage.viewmodel.CheckListPageViewModel
import com.smilearts.smilenotes.main.fragment.BFilterOptionFrag
import com.smilearts.smilenotes.model.CheckListModel
import com.smilearts.smilenotes.repository.RepositoryUtil
import com.smilearts.smilenotes.util.AppUtil
import com.smilearts.smilenotes.util.TempData

class LandingPageCheckList : AppCompatActivity() {

    private val TAG = "LandingPageCheckList"

    private lateinit var binding: CheckListLandingPageBinding
    private lateinit var viewModel: CheckListPageViewModel
    private lateinit var repositoryUtil: RepositoryUtil
    private lateinit var checkAdapter: CheckListMainAdapter
    private var bFilter: BFilterOptionFrag? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CheckListLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repositoryUtil = RepositoryUtil(this, TempData(applicationContext))
        viewModel = ViewModelProvider(this, BaseClass(this, repositoryUtil))[CheckListPageViewModel::class.java]

        checkAdapter = CheckListMainAdapter(arrayListOf(), object : CheckListCallBack {
            override fun optionMenu(checkList: CheckListModel) {
                viewModel.checkListOptionScreen(checkList)
            }

            override fun moveNextPage(checkList: CheckListModel) {
                val intent = Intent(applicationContext, EditCheckListPage::class.java)
                intent.putExtra(AppUtil.TYPE, AppUtil.UPDATE_PAGE)
                intent.putExtra(AppUtil.CHECK_LIST_MODEL, checkList)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

            override fun setChooseList(checkList: CheckListModel) {
                viewModel.checkListDetails(checkList)
            }
        })
        binding.checkListPageCheckList.setHasFixedSize(true)
        binding.checkListPageCheckList.adapter = checkAdapter

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()

        showCheckList(AppUtil.ALL_CHECK, "", "")

        binding.checkListPageSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                checkAdapter!!.filter.filter(newText)
                return false
            }
        })

        binding.checkListPageAddCheckList.setOnClickListener {
            viewModel.editCheckListScreen()
        }

        binding.checkListPageNoDataImage.setOnClickListener {
            viewModel.editCheckListScreen()
        }

        binding.checkListPageNoDataText.setOnClickListener {
            viewModel.editCheckListScreen()
        }

        binding.checkListPageRefresh.setOnClickListener {
            showCheckList(AppUtil.ALL_CHECK, "","")
        }

        binding.checkListPageFilter.setOnClickListener {
            showBottomFilterScreen()
        }

        repositoryUtil.errorStatus.observe(this) {
            if (it.isNotEmpty()) showMessage(it)
        }

    }

    private fun showCheckList(type: String, shortingType: String, shortName: String?) {
        when(type) {
            AppUtil.ALL_CHECK -> {
                repositoryUtil.checkListRep.getAllCheckList().observe(this) {
                    if (!it.isNullOrEmpty()) {
                        binding.checkListPageCheckList.visibility = View.VISIBLE
                        binding.checkListPageNoData.visibility = View.GONE
                        checkAdapter?.updateDetails(it as ArrayList<CheckListModel>)
                    } else {
                        binding.checkListPageCheckList.visibility = View.GONE
                        binding.checkListPageNoData.visibility = View.VISIBLE
                        checkAdapter?.updateDetails(arrayListOf())
                    }
                }
            }
            AppUtil.SHORT_CHECK -> {
                when (shortingType) {
                    AppUtil.SHORT_COLOR -> repositoryUtil!!.checkListRep.shortBasedColor(shortName!!).observe(this) {
                        if (!it.isNullOrEmpty()) {
                            binding.checkListPageCheckList.visibility = View.VISIBLE
                            binding.checkListPageNoData.visibility = View.GONE
                            checkAdapter?.updateDetails(it as ArrayList<CheckListModel>)
                        } else {
                            binding.checkListPageCheckList.visibility = View.GONE
                            binding.checkListPageNoData.visibility = View.VISIBLE
                            checkAdapter?.updateDetails(arrayListOf())
                        }
                    }
                    AppUtil.SHORT_PRI -> repositoryUtil!!.checkListRep.shortPriority(shortName!!.toInt()).observe(this) {
                        if (!it.isNullOrEmpty()) {
                            binding.checkListPageCheckList.visibility = View.VISIBLE
                            binding.checkListPageNoData.visibility = View.GONE
                            checkAdapter?.updateDetails(it as ArrayList<CheckListModel>)
                        } else {
                            binding.checkListPageCheckList.visibility = View.GONE
                            binding.checkListPageNoData.visibility = View.VISIBLE
                            checkAdapter?.updateDetails(arrayListOf())
                        }
                    }
                    AppUtil.SHORT_ACENTING -> repositoryUtil!!.checkListRep.shortBasedOrder(shortName!!).observe(this) {
                        if (!it.isNullOrEmpty()) {
                            binding.checkListPageCheckList.visibility = View.VISIBLE
                            binding.checkListPageNoData.visibility = View.GONE
                            checkAdapter?.updateDetails(it as ArrayList<CheckListModel>)
                        } else {
                            binding.checkListPageCheckList.visibility = View.GONE
                            binding.checkListPageNoData.visibility = View.VISIBLE
                            checkAdapter?.updateDetails(arrayListOf())
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return false
    }

    private fun showBottomFilterScreen() {
        bFilter = BFilterOptionFrag("Show all check list",object : FilterCallBack {
            override fun shortPriority(priority: Int) {
                bFilter!!.dismiss()
                showCheckList(AppUtil.SHORT_NOTES, AppUtil.SHORT_PRI, priority.toString())
            }

            override fun shortColor(color: String?) {
                bFilter!!.dismiss()
                showCheckList(AppUtil.SHORT_NOTES, AppUtil.SHORT_COLOR, color)
            }

            override fun shortACDC(acdc: String?) {
                bFilter!!.dismiss()
                showCheckList(AppUtil.SHORT_NOTES, AppUtil.SHORT_ACENTING, acdc)
            }

            override fun allNotes() {
                bFilter!!.dismiss()
                showCheckList(AppUtil.ALL_NOTES, "", "")
            }
        })
        bFilter!!.show(supportFragmentManager, TAG)
    }

    private fun showMessage(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

}