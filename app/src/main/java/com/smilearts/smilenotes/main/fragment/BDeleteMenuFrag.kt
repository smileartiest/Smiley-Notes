package com.smilearts.smilenotes.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smilearts.smilenotes.R
import com.smilearts.smilenotes.databinding.BottomSheetDeleteBinding
import com.smilearts.smilenotes.main.callback.DeleteCallBack

class BDeleteMenuFrag(private val callBack: DeleteCallBack) : BottomSheetDialogFragment() {

    private var binding: BottomSheetDeleteBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomSheetDeleteBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        binding!!.bottomSheetDeleteNavView.setNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.recycle_menu_delete -> {
                    callBack.delete()
                    return@setNavigationItemSelectedListener true
                }

                R.id.recycle_menu_restore -> {
                    callBack.restore()
                    return@setNavigationItemSelectedListener true
                }
            }
            false
        }
    }
}