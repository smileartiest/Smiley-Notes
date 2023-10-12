package com.smilearts.smilenotes.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smilearts.smilenotes.R
import com.smilearts.smilenotes.databinding.BottomSheetNotesOptionsBinding
import com.smilearts.smilenotes.main.notespage.viewmodel.NotesPageViewModel
import com.smilearts.smilenotes.util.AppUtil

class BNotesPageOptionsFrag(
    private val owner: LifecycleOwner,
    private val viewModel: NotesPageViewModel
) : BottomSheetDialogFragment() {
    private var binding: BottomSheetNotesOptionsBinding? = null
    private var priority = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetNotesOptionsBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.liveNotesModel.observe(owner) { notesModel ->
            if (notesModel != null) {
                if (notesModel.priority == 0) {
                    priority = notesModel.priority
                    binding!!.btSheetPriorityIcon.setImageResource(R.drawable.non_priority_icon)
                } else {
                    priority = notesModel.priority
                    binding!!.btSheetPriorityIcon.setImageResource(R.drawable.priority_icon)
                }
            }
        }
        binding!!.btSheetPriorityIcon.setOnClickListener { v: View? ->
            if (priority == 0) {
                viewModel.setPriority(1)
            } else {
                viewModel.setPriority(0)
            }
        }
        binding!!.btSheetDefaultBgIcon.setOnClickListener { viewModel.setBG(AppUtil.DEFAULT) }
        binding!!.btSheetLightthemeBgIcon.setOnClickListener { viewModel.setBG(AppUtil.LIGHTTHEME) }
        binding!!.btSheetLightblueBgIcon.setOnClickListener { viewModel.setBG(AppUtil.LIGHTBLUE) }
        binding!!.btSheetLightgreenBgIcon.setOnClickListener { viewModel.setBG(AppUtil.LIGHTGREEN) }
        binding!!.btSheetLightyellowBgIcon.setOnClickListener { viewModel.setBG(AppUtil.LIGHTYELLOW) }
        binding!!.btSheetDarkyellowBgIcon.setOnClickListener { viewModel.setBG(AppUtil.DARKYELLOW) }
        binding!!.btSheetDelete.setOnClickListener { viewModel.delete() }
        binding!!.btSheetBin.setOnClickListener { viewModel.moveBin() }
        binding!!.btSheetShare.setOnClickListener { viewModel.share() }
    }
}