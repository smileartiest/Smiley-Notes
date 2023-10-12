package com.smilearts.smilenotes.main.mainpage

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.smilearts.smilenotes.R
import com.smilearts.smilenotes.databinding.MainLandingPageBinding
import com.smilearts.smilenotes.main.callback.MainMenuCallBack
import com.smilearts.smilenotes.main.baseclass.BaseClass
import com.smilearts.smilenotes.main.fragment.BMainMenuFrag
import com.smilearts.smilenotes.main.mainpage.viewmodel.MainPageViewModel
import com.smilearts.smilenotes.repository.RepositoryUtil
import com.smilearts.smilenotes.util.AppUtil
import com.smilearts.smilenotes.util.TempData

class LandingPageMain : AppCompatActivity() {

    private var viewModel: MainPageViewModel? = null
    private var repositoryUtil: RepositoryUtil? = null
    private var binding: MainLandingPageBinding? = null
    private var bScreen: BMainMenuFrag? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLandingPageBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setSupportActionBar(binding!!.mainAppbar)

        binding!!.mainAppbar.navigationIcon!!.setColorFilter(resources.getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP)

        repositoryUtil = RepositoryUtil(this, TempData(applicationContext))

        viewModel = ViewModelProvider(this, BaseClass(this, repositoryUtil!!))[MainPageViewModel::class.java]

        repositoryUtil!!.errorStatus.observe(this) { s: String? ->
            binding!!.mainAppbar.performShow()
            Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()

        binding!!.mainLandingPageAddNotes.setOnClickListener {
            viewModel!!.addNotesScreen()
        }

        binding!!.mainLandingPageNotesMenu.setOnClickListener {
            viewModel?.notesScreen()
        }

        binding!!.mainLandingPageCheckListMenu.setOnClickListener {
            viewModel?.checkListScreen()
        }

        binding!!.mainAppbar.setNavigationOnClickListener { showBottomMainScreen() }

        repositoryUtil?.notesRep?.getAllNotes()?.observe(this) {
            if (it != null) binding!!.mainLandingPageNotesMenuCount.text = "${it.size}"
            else binding!!.mainLandingPageNotesMenuCount.text = "0"
        }

        repositoryUtil?.checkListRep?.getAllCheckList()?.observe(this) {
            if (it != null) binding!!.mainLandingPageCheckListCount.text = "${it.size}"
            else binding!!.mainLandingPageCheckListCount.text = "0"
        }

    }

    private fun showBottomMainScreen() {
        bScreen = BMainMenuFrag(object : MainMenuCallBack {
            override fun chooseType(type: String?) {
                when (type) {
                    AppUtil.MAIN_MENU_ADD_NOTES -> {
                        bScreen!!.dismiss()
                        repositoryUtil!!.notesRep.cancelAll()
                        viewModel!!.addNotesScreen()
                    }

                    AppUtil.MAIN_MENU_CHECK_LIST -> {
                        bScreen!!.dismiss()
                        viewModel!!.addCheckListScreen()
                    }

                    AppUtil.MAIN_MENU_SETTINGS -> {
                        bScreen!!.dismiss()
                        repositoryUtil!!.notesRep.cancelAll()
                        viewModel!!.settingScreen()
                    }

                    AppUtil.MAIN_MENU_BIN -> {
                        bScreen!!.dismiss()
                        repositoryUtil!!.notesRep.cancelAll()
                        viewModel!!.binScreen()
                    }
                }
            }
        })
        bScreen!!.show(supportFragmentManager, TAG)
    }

    override fun onPause() {
        super.onPause()
        if (bScreen != null) bScreen!!.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_setting -> {
                viewModel!!.settingScreen()
            }

            R.id.main_bin -> {
                viewModel!!.binScreen()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val ad = AlertDialog.Builder(this@LandingPageMain)
        ad.setTitle("Close !")
        ad.setMessage("Are you sure want to close this app ?")
        ad.setPositiveButton("Close") { dialog, which ->
            dialog.dismiss()
            finishAffinity()
        }
        ad.setNegativeButton("Not Now") { dialog, which -> dialog.dismiss() }
        ad.show()
    }

    companion object {
        private const val TAG = "Main Page"
    }

}