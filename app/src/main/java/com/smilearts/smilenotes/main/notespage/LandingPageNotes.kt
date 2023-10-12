package com.smilearts.smilenotes.main.notespage

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.smilearts.smilenotes.R
import com.smilearts.smilenotes.databinding.NotesLandingPageBinding
import com.smilearts.smilenotes.main.notespage.adapter.NotesAdapter
import com.smilearts.smilenotes.main.callback.FilterCallBack
import com.smilearts.smilenotes.main.callback.NotesCallBack
import com.smilearts.smilenotes.main.baseclass.BaseClass
import com.smilearts.smilenotes.main.fragment.BFilterOptionFrag
import com.smilearts.smilenotes.main.notespage.screens.NotesEditPage
import com.smilearts.smilenotes.main.notespage.viewmodel.NotesPageViewModel
import com.smilearts.smilenotes.model.NotesModel
import com.smilearts.smilenotes.repository.RepositoryUtil
import com.smilearts.smilenotes.util.AppUtil
import com.smilearts.smilenotes.util.TempData

class LandingPageNotes : AppCompatActivity() {

    private lateinit var binding: NotesLandingPageBinding
    private var viewModel: NotesPageViewModel? = null
    private var repositoryUtil: RepositoryUtil? = null
    private var bFilter: BFilterOptionFrag? = null
    private var notesAdapter: NotesAdapter? = null

    private val TAG = "LandingPageNotes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NotesLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repositoryUtil = RepositoryUtil(this, TempData(applicationContext))

        viewModel = ViewModelProvider(this, BaseClass(this, repositoryUtil!!))[NotesPageViewModel::class.java]

        repositoryUtil!!.errorStatus.observe(this) { s: String? ->
            Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
        }

        notesAdapter = NotesAdapter(arrayListOf(), object : NotesCallBack {
                override fun moveToNotesPage(model: NotesModel?) {
                    val intent = Intent(applicationContext, NotesEditPage::class.java)
                    intent.putExtra(AppUtil.TYPE, AppUtil.UPDATE_PAGE)
                    intent.putExtra(AppUtil.NOTES_MODEL, model)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }

                override fun setChooseList(model: NotesModel?) {
                    repositoryUtil!!.notesRep.setChooseList(model!!)
                }

                override fun notesOptionScreen(model: NotesModel?) {
                    viewModel!!.notesOptionScreen(model!!)
                }
            })
        binding.notesLandingPageRecycle.setHasFixedSize(true)
        binding.notesLandingPageRecycle.adapter = notesAdapter

    }

    override fun onResume() {
        super.onResume()

        showNotesList(AppUtil.ALL_NOTES,"","")

        repositoryUtil!!.notesRep.chooseMenu.observe(this) { aBoolean: Boolean ->
            if (notesAdapter != null) notesAdapter!!.setChooseState(
                repositoryUtil!!.notesRep.chooseStatus
            )
            if (aBoolean) {
                binding.notesLandingPageAddNotes.setImageResource(R.drawable.delete_icon)
            } else {
                binding.notesLandingPageAddNotes.setImageResource(R.drawable.add_notes_icon)
            }
        }

        binding.notesLandingPageSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                notesAdapter!!.filter.filter(newText)
                return false
            }
        })

        findViewById<View>(R.id.notes_landing_page_nodata_add_notes_src).setOnClickListener { v: View? ->
            repositoryUtil!!.notesRep.cancelAll()
            if (notesAdapter != null) notesAdapter!!.setChooseState(false)
            repositoryUtil!!.notesRep.chooseMenu.postValue(false)
            viewModel!!.notesScreen()
        }

        findViewById<View>(R.id.notes_landing_page_nodata_add_notes).setOnClickListener { v: View? ->
            repositoryUtil!!.notesRep.cancelAll()
            if (notesAdapter != null) notesAdapter!!.setChooseState(false)
            repositoryUtil!!.notesRep.chooseMenu.postValue(false)
            viewModel!!.notesScreen()
        }

        binding.notesLandingPageFilter.setOnClickListener {
            showBottomFilterScreen()
        }

        binding.notesLandingPageNodataRefresh.setOnClickListener {
            showNotesList(AppUtil.ALL_NOTES,"","")
        }

        binding.notesLandingPageAddNotes.setOnClickListener { v: View? ->
            if (repositoryUtil!!.notesRep.chooseStatus) {
                val ad = AlertDialog.Builder(this@LandingPageNotes)
                ad.setTitle("Delete !")
                ad.setMessage("Are you sure want to Delete this notes ? \nTotal delete count : ${repositoryUtil!!.notesRep.chooseList.size}")
                ad.setPositiveButton("Delete") { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                    repositoryUtil!!.notesRep.deleteAll()
                }
                ad.setNeutralButton("Move Recycle") { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                    repositoryUtil!!.notesRep.moveBin()
                }
                ad.setNegativeButton("Not Now") { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                    repositoryUtil!!.notesRep.cancelAll()
                }
                ad.show()
            } else {
                repositoryUtil!!.notesRep.cancelAll()
                viewModel!!.notesScreen()
            }
        }

    }

    private fun showBottomFilterScreen() {
        bFilter = BFilterOptionFrag("Show all notes",object : FilterCallBack {
            override fun shortPriority(priority: Int) {
                bFilter!!.dismiss()
                showNotesList(AppUtil.SHORT_NOTES, AppUtil.SHORT_PRI, priority.toString())
            }

            override fun shortColor(color: String?) {
                bFilter!!.dismiss()
                showNotesList(AppUtil.SHORT_NOTES, AppUtil.SHORT_COLOR, color)
            }

            override fun shortACDC(acdc: String?) {
                bFilter!!.dismiss()
                showNotesList(AppUtil.SHORT_NOTES, AppUtil.SHORT_ACENTING, acdc)
            }

            override fun allNotes() {
                bFilter!!.dismiss()
                showNotesList(AppUtil.ALL_NOTES, "", "")
            }
        })
        bFilter!!.show(supportFragmentManager, TAG)
    }

    private fun showNotesList(type: String, shortingType: String, shortName: String?) {
        when (type) {
            AppUtil.ALL_NOTES -> repositoryUtil!!.notesRep.getAllNotes()
                .observe(this) { notesModels: List<NotesModel?>? ->
                    if (notesModels == null) {
                        binding.notesLandingPageRecycle.visibility = View.GONE
                        binding.notesLandingPageNodata.visibility = View.VISIBLE
                        findViewById<View>(R.id.notes_landing_page_nodata_refresh).visibility = View.INVISIBLE
                    } else {
                        binding.notesLandingPageRecycle.visibility = View.VISIBLE
                        binding.notesLandingPageNodata.visibility = View.GONE
                        notesAdapter?.updateDetails(notesModels as ArrayList<NotesModel>)
                    }
                }
            AppUtil.SHORT_NOTES ->
                when (shortingType) {
                    AppUtil.SHORT_COLOR -> repositoryUtil!!.notesRep.shortBasedColor(shortName!!)
                        .observe(this) { notesModels: List<NotesModel?>? ->
                            if (notesModels.isNullOrEmpty()) {
                                binding.notesLandingPageRecycle.visibility = View.GONE
                                binding.notesLandingPageNodata.visibility = View.VISIBLE
                                findViewById<View>(R.id.notes_landing_page_nodata_refresh).visibility =
                                    View.VISIBLE
                            } else {
                                binding.notesLandingPageRecycle.visibility = View.VISIBLE
                                binding.notesLandingPageNodata.visibility = View.GONE
                                notesAdapter?.updateDetails(notesModels as ArrayList<NotesModel>)
                            }
                        }
                    AppUtil.SHORT_PRI -> repositoryUtil!!.notesRep.shortPriority(shortName!!.toInt())
                        .observe(this) { notesModels: List<NotesModel?>? ->
                            if (notesModels.isNullOrEmpty()) {
                                binding.notesLandingPageRecycle.visibility = View.GONE
                                binding.notesLandingPageNodata.visibility = View.VISIBLE
                                findViewById<View>(R.id.notes_landing_page_nodata_refresh).visibility =
                                    View.VISIBLE
                            } else {
                                binding.notesLandingPageRecycle.visibility = View.VISIBLE
                                binding.notesLandingPageNodata.visibility = View.GONE
                                notesAdapter?.updateDetails(notesModels as ArrayList<NotesModel>)
                            }
                        }
                    AppUtil.SHORT_ACENTING -> repositoryUtil!!.notesRep.shortBasedOrder(shortName!!)
                        .observe(this) { notesModels: List<NotesModel?>? ->
                            if (notesModels.isNullOrEmpty()) {
                                binding.notesLandingPageRecycle.visibility = View.GONE
                                binding.notesLandingPageNodata.visibility = View.VISIBLE
                                findViewById<View>(R.id.notes_landing_page_nodata_refresh).visibility = View.VISIBLE
                            } else {
                                binding.notesLandingPageRecycle.visibility = View.VISIBLE
                                binding.notesLandingPageNodata.visibility = View.GONE
                                notesAdapter?.updateDetails(notesModels as ArrayList<NotesModel>)
                            }
                        }
                }
        }
    }

    override fun onPause() {
        super.onPause()
        if (bFilter != null) bFilter!!.dismiss()
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

}