package com.smilearts.smilenotes.main

import android.app.KeyguardManager
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.smilearts.smilenotes.databinding.SecretPinNumberBinding
import com.smilearts.smilenotes.main.mainpage.LandingPageMain
import com.smilearts.smilenotes.util.TempData

class SecretPinNumber : AppCompatActivity() {

    var tempData: TempData? = null
    var count = 0
    private var binding: SecretPinNumberBinding? = null
    var loading: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SecretPinNumberBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        tempData = TempData(this@SecretPinNumber)
        loading = ProgressDialog(this)
    }

    override fun onStart() {
        super.onStart()
        if (tempData!!.getPinStatus() && tempData!!.getFingerPrintStatus()) {
            binding!!.pinPinpad.visibility = View.VISIBLE
            binding!!.pinPinnumber.visibility = View.VISIBLE
            binding!!.pinPinnumber.isClickable = true
            binding!!.pinMobilepin.visibility = View.VISIBLE
        } else if (tempData!!.getPinStatus() && !tempData!!.getFingerPrintStatus()) {
            binding!!.pinPinpad.visibility = View.VISIBLE
            binding!!.pinPinnumber.visibility = View.VISIBLE
            binding!!.pinPinnumber.isClickable = true
            binding!!.pinMobilepin.visibility = View.INVISIBLE
        } else if (!tempData!!.getPinStatus() && tempData!!.getFingerPrintStatus()) {
            binding!!.pinPinpad.visibility = View.INVISIBLE
            binding!!.pinPinnumber.visibility = View.INVISIBLE
            binding!!.pinPinnumber.isClickable = false
            binding!!.pinMobilepin.visibility = View.VISIBLE
            openFinger()
        }
    }

    override fun onResume() {
        super.onResume()
        binding!!.pinMobilepin.setOnClickListener { v: View? -> openFinger() }
        binding!!.pinContinue.setOnClickListener { v: View? ->
            val pin1 = binding!!.pinPinnumber.text.toString()
            if (pin1.length == 4) {
                loading!!.show()
                CheckPin(pin1)
            } else if (pin1.length == 6) {
                loading!!.show()
                CheckPin(pin1)
            } else if (pin1.length == 0) {
                binding!!.pinPinnumber.hint = "Pin Number"
            } else {
                Snackbar.make(v!!, "Please enter valid pin number", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding!!.pinClear.setOnClickListener { v: View? ->
            val stringBuffer = StringBuffer(
                binding!!.pinPinnumber.text.toString()
            )
            if (stringBuffer.length == 0) {
                binding!!.pinPinnumber.hint = "Pin Number"
            } else if (stringBuffer.length == 1) {
                binding!!.pinPinnumber.setText("")
                binding!!.pinPinnumber.hint = "Pin Number"
            } else if (stringBuffer.length > 1) {
                stringBuffer.deleteCharAt(stringBuffer.length - 1)
                binding!!.pinPinnumber.setText(stringBuffer.toString())
            }
        }
        binding!!.pinPin0.setOnClickListener { v: View? ->
            val temp = binding!!.pinPinnumber.text.toString()
            binding!!.pinPinnumber.setText(temp + "0")
        }
        binding!!.pinPin1.setOnClickListener { v: View? ->
            val temp = binding!!.pinPinnumber.text.toString()
            binding!!.pinPinnumber.setText(temp + "1")
        }
        binding!!.pinPin2.setOnClickListener { v: View? ->
            val temp = binding!!.pinPinnumber.text.toString()
            binding!!.pinPinnumber.setText(temp + "2")
        }
        binding!!.pinPin3.setOnClickListener { v: View? ->
            val temp = binding!!.pinPinnumber.text.toString()
            binding!!.pinPinnumber.setText(temp + "3")
        }
        binding!!.pinPin4.setOnClickListener { v: View? ->
            val temp = binding!!.pinPinnumber.text.toString()
            binding!!.pinPinnumber.setText(temp + "4")
        }
        binding!!.pinPin5.setOnClickListener { v: View? ->
            val temp = binding!!.pinPinnumber.text.toString()
            binding!!.pinPinnumber.setText(temp + "5")
        }
        binding!!.pinPin6.setOnClickListener { v: View? ->
            val temp = binding!!.pinPinnumber.text.toString()
            binding!!.pinPinnumber.setText(temp + "6")
        }
        binding!!.pinPin7.setOnClickListener { v: View? ->
            val temp = binding!!.pinPinnumber.text.toString()
            binding!!.pinPinnumber.setText(temp + "7")
        }
        binding!!.pinPin8.setOnClickListener { v: View? ->
            val temp = binding!!.pinPinnumber.text.toString()
            binding!!.pinPinnumber.setText(temp + "8")
        }
        binding!!.pinPin9.setOnClickListener { v: View? ->
            val temp = binding!!.pinPinnumber.text.toString()
            binding!!.pinPinnumber.setText(temp + "9")
        }
        binding!!.pinPinnumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 4) {
                    loading!!.show()
                    CheckPin(s.toString())
                } else if (s.length > 4) {
                    binding!!.pinPinnumber.error = "Not valid"
                }
            }
        })
    }

    private fun openFinger() {
        val km = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        if (km.isKeyguardSecure) {
            val i = km.createConfirmDeviceCredentialIntent("Authentication required", "password")
            startActivityForResult(i, CODE_AUTHENTICATION_VERIFICATION)
        } else Toast.makeText(
            this@SecretPinNumber,
            "No any security setup done by user(pattern or password or pin or fingerprint",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == CODE_AUTHENTICATION_VERIFICATION) {
            Toast.makeText(this, "Success: Verified user's identity", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, LandingPageMain::class.java))
            finish()
        } else Toast.makeText(this, "Failure: Unable to verify user's identity", Toast.LENGTH_SHORT)
            .show()
    }

    private fun CheckPin(pin: String) {
        if (pin == tempData!!.getPin()) {
            loading!!.dismiss()
            startActivity(Intent(applicationContext, LandingPageMain::class.java))
            finish()
        } else {
            loading!!.dismiss()
            Toast.makeText(this@SecretPinNumber, "Incorrect Pin Number", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        count++
        if (count > 1) {
            finishAffinity()
        } else {
            Toast.makeText(this, "Press again to close", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val CODE_AUTHENTICATION_VERIFICATION = 241
    }

}