package com.smilearts.smilenotes.util

import android.content.Context
import android.content.SharedPreferences

class TempData(mContext: Context) {
    private val sharedPreferences: SharedPreferences =
        mContext.getSharedPreferences("TempData", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun login(UID: String?, Log: Boolean) {
        editor.putString("UID", UID)
        editor.putBoolean("Log", Log)
        editor.commit()
    }

    /*fun storeKey(pubKey: String , prvKey: String) {
        editor.putString(EncryptionClass.PUB_KEY , pubKey)
        editor.putString(EncryptionClass.PRV_KEY , prvKey)
        editor.commit()
    }*/

    /*fun getPublicKey() : String? {
        return sharedPreferences.getString(EncryptionClass.PUB_KEY, null)
    }

    fun getPrivateKey() : String? {
        return sharedPreferences.getString(EncryptionClass.PRV_KEY, null)
    }*/

    fun keyStatus() : Boolean {
        return sharedPreferences.getBoolean("Key" , false)
    }

    fun addWidgetId(id: Int) {
        editor.putInt("WID", id).commit()
    }

    val widgetId: Int
        get() = sharedPreferences.getInt("WID", 0)

    fun enablePin(sts: Boolean) {
        editor.putBoolean("Pin", sts).commit()
    }

    fun addToken(token: String?) {
        editor.putString("TOKEN", token).commit()
    }

    fun setPin(pin: String?) {
        editor.putString("PIN", pin).commit()
    }

    fun getPin() : String? {
        return sharedPreferences.getString("PIN", null)
    }

    fun getPinStatus(): Boolean {
        return sharedPreferences.getBoolean("Pin", false)
    }

    fun setFingerPrint(status: Boolean) {
        editor.putBoolean("Biometric" , status).commit()
    }

    fun getFingerPrintStatus() : Boolean {
        return sharedPreferences.getBoolean("Biometric" ,false)
    }

    fun logout() {
        editor.clear().commit()
    }

    fun getUid() : String? {
        return sharedPreferences.getString("UID", null)
    }

}