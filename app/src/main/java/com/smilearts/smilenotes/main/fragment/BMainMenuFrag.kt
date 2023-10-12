package com.smilearts.smilenotes.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smilearts.smilenotes.R
import com.smilearts.smilenotes.databinding.BottomSheetMainBinding
import com.smilearts.smilenotes.main.callback.MainMenuCallBack
import com.smilearts.smilenotes.util.AppUtil

class BMainMenuFrag(
    private val callBack: MainMenuCallBack
) : BottomSheetDialogFragment() {

    private var binding: BottomSheetMainBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomSheetMainBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        binding!!.bottomSheetNavView.setNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.main_bottom_nav_add_note -> {
                    callBack.chooseType(AppUtil.MAIN_MENU_ADD_NOTES)
                    return@setNavigationItemSelectedListener true
                }

                R.id.main_bottom_nav_add_check_list -> {
                    callBack.chooseType(AppUtil.MAIN_MENU_CHECK_LIST)
                    return@setNavigationItemSelectedListener true
                }

                R.id.main_bottom_nav_bin -> {
                    callBack.chooseType(AppUtil.MAIN_MENU_BIN)
                    return@setNavigationItemSelectedListener true
                }

                R.id.main_bottom_nav_setting -> {
                    callBack.chooseType(AppUtil.MAIN_MENU_SETTINGS)
                    return@setNavigationItemSelectedListener true
                }
            }
            false
        }
    }
}