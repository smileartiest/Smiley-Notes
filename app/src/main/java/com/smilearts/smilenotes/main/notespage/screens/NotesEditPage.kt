package com.smilearts.smilenotes.main.notespage.screens

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.smilearts.smilenotes.R
import com.smilearts.smilenotes.databinding.ActivityNotesEditPageBinding
import com.smilearts.smilenotes.main.baseclass.BaseClass
import com.smilearts.smilenotes.main.fragment.BNotesPageOptionsFrag
import com.smilearts.smilenotes.main.notespage.viewmodel.NotesPageViewModel
import com.smilearts.smilenotes.model.NotesModel
import com.smilearts.smilenotes.repository.RepositoryUtil
import com.smilearts.smilenotes.util.AppUtil
import com.smilearts.smilenotes.util.TempData

class NotesEditPage : AppCompatActivity() {

    private lateinit var binding: ActivityNotesEditPageBinding
    private var viewModel: NotesPageViewModel? = null
    private var repositoryUtil: RepositoryUtil? = null
    private var bOption: BNotesPageOptionsFrag? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesEditPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding!!.notesToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = " "

        binding!!.notesToolbar.navigationIcon!!.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
        binding!!.notesToolbar.overflowIcon!!.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)

        repositoryUtil = RepositoryUtil(this, TempData(applicationContext))

        viewModel = ViewModelProvider(this, BaseClass(this, repositoryUtil!!))[NotesPageViewModel::class.java]
        viewModel!!.setType(intent)

        viewModel!!.liveNotesModel.observe(this) { notes: NotesModel? ->
            if (notes != null) {
                if (bOption != null) bOption!!.dismiss()
                if (notes.title != null) binding!!.notesTxtTitle.setText(notes.title)
                if (notes.message != null) binding!!.notesTxtMessage.setText(notes.message)
                when (notes.bg) {
                    AppUtil.LIGHTYELLOW -> binding!!.notesPageScreen.setBackgroundResource(R.color.LIGHTYELLOW)
                    AppUtil.DEFAULT -> binding!!.notesPageScreen.setBackgroundResource(R.color.DEFAULT)
                    AppUtil.LIGHTTHEME -> binding!!.notesPageScreen.setBackgroundResource(R.color.LIGHTTHEME)
                    AppUtil.LIGHTBLUE -> binding!!.notesPageScreen.setBackgroundResource(R.color.LIGHTBLUE)
                    AppUtil.LIGHTGREEN -> binding!!.notesPageScreen.setBackgroundResource(R.color.LIGHTGREEN)
                    AppUtil.DARKYELLOW -> binding!!.notesPageScreen.setBackgroundResource(R.color.DARKYELLOW)
                }
                if (notes.date.isNotEmpty() && notes.time.isNotEmpty()) {
                    supportActionBar!!.title = "Update Details"
                    supportActionBar!!.subtitle = "Last Edited on ${notes.date} Time ${notes.time}"
                } else {
                    supportActionBar!!.title = "Enter Details"
                }
            }
        }

        viewModel!!.screenCloseStatus.observe(this) { status: Boolean ->
            if (status) {
                finish()
            }
        }

        binding!!.notesTxtTitle.requestFocus()

    }

    override fun onStart() {
        super.onStart()

        viewModel!!.notesRefresh()

        binding!!.notesToolbar.setNavigationOnClickListener { v: View? -> onBackPressed() }

        binding!!.notesTxtTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel!!.setTitle(s.toString())
            }
        })

        binding!!.notesTxtMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel!!.setMessage(s.toString())
            }
        })

    }

    override fun onPause() {
        super.onPause()

        viewModel!!.resumeScreen = true

        if (binding!!.notesTxtTitle.text.toString().isNotEmpty() && binding!!.notesTxtMessage.text.toString().isNotEmpty()) {
            viewModel!!.setTitle(binding!!.notesTxtTitle.text.toString())
            viewModel!!.setMessage(binding!!.notesTxtMessage.text.toString())
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.notes_page_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.notes_page_more -> showOptionDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showOptionDialog() {
        bOption = BNotesPageOptionsFrag(this, viewModel!!)
        bOption!!.show(supportFragmentManager, TAG)
    }

    override fun onBackPressed() {
        if (viewModel!!.updateStatus) {
            if ((binding!!.notesTxtTitle.text != null && binding!!.notesTxtMessage.text != null) || (binding!!.notesTxtTitle.text != null || binding!!.notesTxtMessage.text != null)) {
                openDialog()
            } else {
                finish()
            }
        } else {
            finish()
        }
    }

    private fun openDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Notes")
        dialog.setMessage("Save your Notes ?")
        dialog.setPositiveButton("Save") { dialog1: DialogInterface, which: Int ->
            viewModel!!.notesModel!!.title = binding!!.notesTxtTitle.text.toString()
            viewModel!!.notesModel!!.message = binding!!.notesTxtMessage.text.toString()
            dialog1.cancel()
            viewModel!!.backPress()
        }
        dialog.setNegativeButton("Discard") { dialog1: DialogInterface, which: Int ->
            dialog1.dismiss()
            finish()
        }
        dialog.show()
    }

    companion object {
        private const val TAG = "Notes Page"
    }

}