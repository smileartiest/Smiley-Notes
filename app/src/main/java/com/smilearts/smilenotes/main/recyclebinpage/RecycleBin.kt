package com.smilearts.smilenotes.main.recyclebinpage

import android.content.DialogInterface
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.smilearts.smilenotes.R
import com.smilearts.smilenotes.databinding.RecycleBinBinding
import com.smilearts.smilenotes.main.recyclebinpage.adapter.RecycleBinAdapter
import com.smilearts.smilenotes.main.callback.DeleteCallBack
import com.smilearts.smilenotes.main.callback.RecycleCallBack
import com.smilearts.smilenotes.main.fragment.BDeleteMenuFrag
import com.smilearts.smilenotes.model.BackUpModel
import com.smilearts.smilenotes.model.RecycleModel
import com.smilearts.smilenotes.repository.RepositoryUtil
import com.smilearts.smilenotes.util.TempData
import java.util.Objects

class RecycleBin : AppCompatActivity() {

    private var binding: RecycleBinBinding? = null
    private var repositoryUtil: RepositoryUtil? = null
    private var bDelete: BDeleteMenuFrag? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RecycleBinBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.recyclebinToolbar)
        supportActionBar!!.title = "Recycle Bin"
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        Objects.requireNonNull(binding!!.recyclebinToolbar.navigationIcon)?.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
        binding!!.recyclebinList.setHasFixedSize(true)
        repositoryUtil = RepositoryUtil(this, TempData(applicationContext))
    }

    override fun onResume() {
        super.onResume()
        binding!!.recyclebinToolbar.setNavigationOnClickListener { onBackPressed() }
        repositoryUtil!!.recycleBinRep.getBinList().observe(this) {
            if (it != null) {
                val adapter = RecycleBinAdapter(it, object : RecycleCallBack {
                    override fun chooseNote(model: RecycleModel?) {
                        openOption(model)
                    }

                    override fun chooseNote(model: BackUpModel?) {
                        //nothing
                    }
                })
                binding!!.recyclebinList.adapter = adapter
                binding!!.recyclebinList.visibility = View.VISIBLE
                binding!!.recyclebinNodata.visibility = View.GONE
                supportActionBar!!.setSubtitle("Total Count . " + it.size)
            } else {
                binding!!.recyclebinList.visibility = View.GONE
                binding!!.recyclebinNodata.visibility = View.VISIBLE
                supportActionBar!!.setSubtitle("Total Count . 0")
            }
        }
        repositoryUtil!!.errorStatus.observe(this) { s: String? ->
            Snackbar.make(binding!!.recycleBinMain, s!!, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.recycle_bin_menu, menu)
        repositoryUtil!!.recycleBinRep.getBinList()
            .observe(this) { recycleModels: List<RecycleModel?>? ->
                menu.findItem(R.id.recycle_bin_menu_delete).isVisible = recycleModels != null
            }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.recycle_bin_menu_delete -> openDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openOption(model: RecycleModel?) {
        bDelete = BDeleteMenuFrag(object : DeleteCallBack {
            override fun delete() {
                bDelete!!.dismiss()
                conformationDialogDelete(model)
            }

            override fun restore() {
                bDelete!!.dismiss()
                conformationDialogRestore(model)
            }
        })
        bDelete!!.show(supportFragmentManager, "Delete")
    }

    private fun conformationDialogDelete(model: RecycleModel?) {
        val ad = AlertDialog.Builder(this)
        ad.setTitle("Delete !")
        ad.setMessage("Are you conform to delete this note ?")
        ad.setPositiveButton("Delete") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
            repositoryUtil!!.recycleBinRep.removeRecycleBin(model!!)
        }
        ad.setNegativeButton("Not now") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        ad.show()
    }

    private fun conformationDialogRestore(model: RecycleModel?) {
        val ad = AlertDialog.Builder(this)
        ad.setTitle("Restore !")
        ad.setMessage("Are you conform to restore this note ?")
        ad.setPositiveButton("Restore") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
            repositoryUtil!!.recycleBinRep.insertNotes(model!!)
        }
        ad.setNegativeButton("Not now") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        ad.show()
    }

    private fun openDialog() {
        val ad = AlertDialog.Builder(this)
        ad.setTitle("Delete !")
        ad.setMessage("Are you conform to delete all notes ?")
        ad.setPositiveButton("Delete All") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
            repositoryUtil!!.recycleBinRep.emptyRecycleBin()
        }
        ad.setNegativeButton("Not now") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        ad.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}