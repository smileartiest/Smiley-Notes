package com.smilearts.smilenotes.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smilearts.smilenotes.databinding.BottomSheetFilterBinding
import com.smilearts.smilenotes.main.callback.FilterCallBack
import com.smilearts.smilenotes.util.AppUtil

class BFilterOptionFrag(private val title: String, private val callBack: FilterCallBack) : BottomSheetDialogFragment() {

    private var binding: BottomSheetFilterBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomSheetFilterBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        binding!!.filterAllNotes.text = title
        binding!!.filterPriority.setOnClickListener { callBack.shortPriority(1) }
        binding!!.filterNonPriority.setOnClickListener { callBack.shortPriority(0) }
        binding!!.filterDefaultColor.setOnClickListener { callBack.shortColor(AppUtil.DEFAULT) }
        binding!!.filterLightThemeColor.setOnClickListener { callBack.shortColor(AppUtil.LIGHTTHEME) }
        binding!!.filterLightBlueColor.setOnClickListener { callBack.shortColor(AppUtil.LIGHTBLUE) }
        binding!!.filterLightGreenColor.setOnClickListener { callBack.shortColor(AppUtil.LIGHTGREEN) }
        binding!!.filterLightYellowColor.setOnClickListener { callBack.shortColor(AppUtil.LIGHTYELLOW) }
        binding!!.filterDarkYellowColor.setOnClickListener { callBack.shortColor(AppUtil.DARKYELLOW) }
        binding!!.filterAcenting.setOnClickListener { callBack.shortACDC(AppUtil.ACENTING) }
        binding!!.filterDecenting.setOnClickListener { callBack.shortACDC(AppUtil.DECENTING) }
        binding!!.filterAllNotes.setOnClickListener { callBack.allNotes() }
    }
}