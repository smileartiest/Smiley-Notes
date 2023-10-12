package com.smilearts.smilenotes.main.checklistpage.screens.adapter

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

class AddCheckListAdapter(
    private var checkList: ArrayList<CheckListModel.Points>,
    private val callback: CheckListCallBack,
    private val checkBox: Boolean
) : RecyclerView.Adapter<AddCheckListAdapter.MyViewHolder>() {

    private val listAll = checkList

    public fun updateList(updateList: ArrayList<CheckListModel.Points>) {
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
        if (!checkBox) holder.statsCheck.visibility = View.GONE
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