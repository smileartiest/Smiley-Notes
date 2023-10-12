package com.smilearts.smilenotes.main

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.BuildConfig
import com.smilearts.smilenotes.R
import com.smilearts.smilenotes.databinding.SettingPageBinding
import com.smilearts.smilenotes.repository.RepositoryUtil
import com.smilearts.smilenotes.util.AppUtil
import com.smilearts.smilenotes.util.DataStorage
import com.smilearts.smilenotes.util.PathUtils
import com.smilearts.smilenotes.util.TempData
import java.util.Calendar
import java.util.Objects

class SettingPage : AppCompatActivity() {

    private var binding: SettingPageBinding? = null
    var tempData: TempData? = null
    var loading: ProgressDialog? = null
    private var repositoryUtil: RepositoryUtil? = null
    private var clickedStatus = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingPageBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initialise()
        repositoryUtil = RepositoryUtil(this, tempData)

        val versionDetail = "version ${com.smilearts.smilenotes.BuildConfig.VERSION_NAME}"
        binding!!.settingsAboutAppVersion.text = versionDetail
        val copyRights = "Copyright ${Calendar.getInstance().get(Calendar.YEAR)} . Smile Artist Tech Coder"
        binding!!.settingsAboutCopyRights.text = copyRights
    }

    override fun onResume() {
        super.onResume()

        setSwitchStatus()

        binding!!.settingsToolbar.setNavigationOnClickListener { onBackPressed() }

        binding!!.settingsRestoreLocalNotes.setOnClickListener {
            var permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
                storagePermission13
            } else {
                storagePermission
            }
            if (permission) {
                val ad = AlertDialog.Builder(this@SettingPage)
                ad.setTitle("Restore Data !")
                ad.setMessage("Are your sure want to restore from your local storage ? \n\nInternal Storage > Documents -> Choose file")
                ad.setPositiveButton("Open Storage") { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                    openFileLocation()
                }
                ad.setNegativeButton("Not now") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
                ad.show()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    enableStoragePermission13()
                } else {
                    enableStoragePermission()
                }
            }
        }

        binding!!.settingsBackupLocalNotes.setOnClickListener { v: View? ->
            val ad = AlertDialog.Builder(this@SettingPage)
            ad.setTitle("Backup Data !")
            ad.setMessage("Are your sure want to backup your current data's ?")
            ad.setPositiveButton("Back up") { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
                var permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
                    storagePermission13
                } else {
                    storagePermission
                }
                if (permission) {
                    storeBackup()
                } else {
                    repositoryUtil!!.errorStatus.postValue("Please Enable Storage Permission")
                    clickedStatus = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        enableStoragePermission13()
                    } else {
                        enableStoragePermission()
                    }
                }
            }
            ad.setNegativeButton("Not now") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
            ad.show()
        }

        binding!!.settingsMoveRecycleAllNotes.setOnClickListener { v: View? ->
            val ad = AlertDialog.Builder(this@SettingPage)
            ad.setTitle("Move Recycle Bin !")
            ad.setMessage("Are you conform to move all notes to Recycle Bin ?")
            ad.setPositiveButton("Move All") { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
                repositoryUtil!!.recycleBinRep.moveAllNotesToRecycleBin()
                Snackbar.make(v!!, " Successful Moved All Notes", Snackbar.LENGTH_SHORT).show()
            }
            ad.setNegativeButton("Not now") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
            ad.show()
        }

        binding!!.settingsClearAllNotes.setOnClickListener { v: View? ->
            val ad = AlertDialog.Builder(this@SettingPage)
            ad.setTitle("Delete !")
            ad.setMessage("Are you conform to delete all notes ?")
            ad.setPositiveButton("Delete All") { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
                repositoryUtil!!.notesRep.deleteAll()
                Snackbar.make(v!!, " Successful Cleared All Notes", Snackbar.LENGTH_SHORT).show()
            }
            ad.setNegativeButton("Not now") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
            ad.show()
        }

        binding!!.settingsClearAllCheckList.setOnClickListener { v: View? ->
            val ad = AlertDialog.Builder(this@SettingPage)
            ad.setTitle("Delete !")
            ad.setMessage("Are you conform to delete all check list ?")
            ad.setPositiveButton("Delete All") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                repositoryUtil!!.checkListRep.clearAllCheckList()
                Snackbar.make(v!!, " Successful Cleared All check list", Snackbar.LENGTH_SHORT).show()
            }
            ad.setNegativeButton("Not now") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
            ad.show()
        }

        binding!!.settingsChangepassword.setOnClickListener { v: View? -> openPasswordDialog() }

        binding!!.settingsShareApp.setOnClickListener { v: View? ->
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Smiley Notes - Handle Day to Day Notes")
            var shareMessage =
                "\nLet me recommend you this application. Best Notes Application in Play Store\n\n"
            shareMessage = """
                ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        }

        binding!!.settingsUpdateApp.setOnClickListener { v: View? ->
            val uri = Uri.parse("market://details?id=" + applicationContext.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + applicationContext.packageName)
                    )
                )
            }
        }

        binding!!.settingsRateApp.setOnClickListener { view: View? ->
            //AppRating(view);
            val uri = Uri.parse("market://details?id=" + applicationContext.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + applicationContext.packageName))
                )
            }
        }

        binding!!.settingsAboutApp.setOnClickListener { view: View? ->
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://smileartiest.com")
                )
            )
        }

        binding!!.settingsAboutCopyRights.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://smileartiest.com")
                )
            )
        }

        binding!!.settingsEnablePassword.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            if (!isChecked) {
                tempData!!.enablePin(false)
                binding!!.settingsEnablePassword.text = "4 digit Password is disabled"
            } else {
                tempData!!.enablePin(true)
                binding!!.settingsEnablePassword.text = "4 digit Password is Enabled"
                openPasswordDialog()
            }
        }

        binding!!.settingsEnableFingerprint.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            if (!isChecked) {
                tempData!!.setFingerPrint(false)
                binding!!.settingsEnableFingerprint.text = "Finger Print is disabled"
            } else {
                tempData!!.setFingerPrint(true)
                binding!!.settingsEnableFingerprint.text = "Finger Print is Enabled"
            }
        }

        repositoryUtil!!.errorStatus.observe(this) { s: String? ->
            if (!s.isNullOrEmpty()) {
                Snackbar.make(findViewById(R.id.settings_mainscreen), s!!, Snackbar.LENGTH_SHORT).show()
                repositoryUtil!!.errorStatus.postValue("")
            }
        }

    }

    private fun setSwitchStatus() {
        if (tempData!!.getPinStatus()) {
            binding!!.settingsEnablePassword.isChecked = true
            binding!!.settingsEnablePassword.text = "4 digit Password is Enabled"
        } else {
            binding!!.settingsEnablePassword.isChecked = false
            binding!!.settingsEnablePassword.text = "4 digit Password is disabled"
        }
        if (tempData!!.getFingerPrintStatus()) {
            binding!!.settingsEnableFingerprint.isChecked = true
            binding!!.settingsEnableFingerprint.text = "Finger Print is Enabled"
        } else {
            binding!!.settingsEnableFingerprint.isChecked = false
            binding!!.settingsEnableFingerprint.text = "Finger Print is disabled"
        }
    }

    private fun storeBackup() {
        val data = repositoryUtil!!.notesRep.getBackupAllNotes()
        if (data != null) {
            loading = ProgressDialog(this)
            showLoading("Backup Please Wait")
            val fileName = AppUtil().getBackupName()
            val path = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Environment.getExternalStorageDirectory().toString() + "/Documents"
            } else {
                Environment.getExternalStoragePublicDirectory("Documents").toString()
            }
            val ret = DataStorage().storeData(fileName, data, path)
            if (ret == 0) {
                repositoryUtil!!.errorStatus.postValue("Store Data Successful File Name : $fileName")
            } else {
                repositoryUtil!!.errorStatus.postValue("Store Data Failed File Name : $fileName")
            }
            loading!!.setMessage("Backup Successful")
            loading!!.dismiss()
        } else {
            repositoryUtil!!.errorStatus.postValue("No Data Found")
        }
    }

    private fun showLoading(message: String) {
        loading!!.setMessage(message)
        loading!!.show()
    }

    private fun openFileLocation() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, READ_STORAGE_REQ)
    }

    private val storagePermission: Boolean
        get() = !(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)

    private val storagePermission13: Boolean
        get() = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED

    private fun enableStoragePermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ),
            MY_PERMISSIONS_REQUEST_STORAGE
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun enableStoragePermission13() {
        ActivityCompat.requestPermissions(this,
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES
            ),
            MY_PERMISSIONS_REQUEST_STORAGE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == READ_STORAGE_REQ) {
            if (data != null) {
                val path = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Environment.getExternalStorageDirectory().toString() + "/Documents"
                } else {
                    Environment.getExternalStoragePublicDirectory("Documents").toString()
                }
                DataStorage().readFromFile(repositoryUtil!!,"$path/${PathUtils.getFileName(applicationContext, data.data)}")
            }
        } else {
            loading!!.dismiss()
            Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == MY_PERMISSIONS_REQUEST_STORAGE) {
            if (permissions[0].equals(Manifest.permission.READ_MEDIA_IMAGES, ignoreCase = true) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                repositoryUtil!!.errorStatus.value = "Permission Granted"
                if (clickedStatus) {
                    clickedStatus = false
                    storeBackup()
                }
            } else if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE, ignoreCase = true) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                repositoryUtil!!.errorStatus.value = "Permission Granted"
                if (clickedStatus) {
                    clickedStatus = false
                    storeBackup()
                }
            } else {
                if (applicationContext == null) {
                    return
                }
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("Permissions Required")
                    .setMessage("You have forcefully denied some of the required permissions for this action. Please open settings, go to permissions and allow them.")
                    .setPositiveButton("Settings") { dialog, _ ->
                        dialog.dismiss()
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                    .setCancelable(false)
                    .create()
                    .show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun openPasswordDialog() {
        val d = Dialog(this@SettingPage)
        d.setContentView(R.layout.dialog_changepassword)
        d.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        d.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        d.setCanceledOnTouchOutside(false)
        val newPassword = d.findViewById<TextInputLayout>(R.id.dialog_password_new)
        val conformPassword = d.findViewById<TextInputLayout>(R.id.dialog_password_conform)
        val complete = d.findViewById<TextView>(R.id.dialog_password_change)
        val cancel = d.findViewById<TextView>(R.id.dialog_password_cancel)
        complete.setOnClickListener { v: View? ->
            if (Objects.requireNonNull(newPassword.editText)?.text.toString() == Objects.requireNonNull(conformPassword.editText)?.text.toString()) {
                tempData!!.setPin(newPassword.editText!!.text.toString())
                Snackbar.make(v!!, "Password change successful", Snackbar.LENGTH_SHORT).show()
                d.dismiss()
                setSwitchStatus()
            } else {
                conformPassword.editText!!.error = "Check Password"
            }
        }
        cancel.setOnClickListener {
            d.cancel()
            tempData!!.enablePin(false)
            setSwitchStatus()
        }
        d.setOnCancelListener { dialog: DialogInterface ->
            dialog.cancel()
            tempData!!.enablePin(false)
            setSwitchStatus()
        }
        d.show()
    }

    private fun initialise() {
        setSupportActionBar(binding!!.settingsToolbar)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        Objects.requireNonNull(binding!!.settingsToolbar.navigationIcon)?.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
        tempData = TempData(this@SettingPage)
        loading = ProgressDialog(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_STORAGE = 10
        private const val READ_STORAGE_REQ = 11
    }
}