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
import com.smilearts.smilenotes.model.CheckListModel

class CheckListRowAdapter(
    private var checkList: ArrayList<CheckListModel.Points>
) : RecyclerView.Adapter<CheckListRowAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_text, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = "${position+1} . ${checkList[position].point}"
        if (checkList[position].checkStatus) {
            holder.title.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    override fun getItemCount(): Int {
        return checkList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.row_test_title)
    }

}