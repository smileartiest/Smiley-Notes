package com.smilearts.smilenotes.main.checklistpage.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smilearts.smilenotes.R
import com.smilearts.smilenotes.main.checklistpage.screens.CheckListCallBack
import com.smilearts.smilenotes.model.CheckListModel

class CheckListActionAdapter(
    private var checkList: ArrayList<CheckListModel.Points>,
    private val callback: CheckListCallBack
) : RecyclerView.Adapter<CheckListActionAdapter.MyViewHolder>() {

    val listAll = checkList

    fun update(updateList: ArrayList<CheckListModel.Points>) {
        checkList = updateList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_check_list, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = checkList[position].point
        holder.statsCheck.setOnClickListener {
            callback.checked(checkList[position], position)
        }
        holder.delete.setOnClickListener {
            callback.remove(checkList[position])
        }
        holder.statsCheck.isChecked = checkList[position].checkStatus
        if (checkList[position].checkStatus) {
            holder.statsCheck.isClickable = false
            holder.title.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.statsCheck.isClickable = true
            holder.title.paintFlags = Paint.ANTI_ALIAS_FLAG
        }
        holder.delete.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return checkList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statsCheck: CheckBox = itemView.findViewById(R.id.row_check_list_check_status)
        val title: TextView = itemView.findViewById(R.id.row_check_list_title)
        val delete: ImageView = itemView.findViewById(R.id.row_check_list_delete)
    }

}