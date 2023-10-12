package com.smilearts.smilenotes.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.smilearts.smilenotes.R
import com.smilearts.smilenotes.databinding.SplashScreenBinding
import com.smilearts.smilenotes.main.mainpage.LandingPageMain
import com.smilearts.smilenotes.repository.RepositoryUtil
import com.smilearts.smilenotes.util.TempData
import java.util.Calendar

class SplashScreen : AppCompatActivity() {

    var tempData: TempData? = null
    lateinit var repositoryUtil: RepositoryUtil
    private var TAG = "Splash Screen"
    private lateinit var binding: SplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tempData = TempData(this@SplashScreen)
        repositoryUtil = RepositoryUtil(this , tempData)

        val copyRights = "Copyright ${Calendar.getInstance().get(Calendar.YEAR)} . Smile Artist Tech Coder"
        binding.splashScreenCopyRights.text = copyRights

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }
                    val token = task.result!!.token
                    Log.d(TAG, token + "," + Build.BRAND + "/" + Build.MODEL)
                })
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        Handler(Looper.getMainLooper()).postDelayed({
            if (tempData!!.getPinStatus() || tempData!!.getFingerPrintStatus()) {
                startActivity(Intent(applicationContext, SecretPinNumber::class.java))
                finish()
            } else {
                startActivity(Intent(applicationContext, LandingPageMain::class.java))
                finish()
            }
        }, 3000)

    }
}