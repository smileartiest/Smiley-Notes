package com.smilearts.smilenotes.main.recyclebinpage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.smilearts.smilenotes.R
import com.smilearts.smilenotes.main.callback.RecycleCallBack
import com.smilearts.smilenotes.model.RecycleModel

class RecycleBinAdapter(
    private val list: List<RecycleModel>,
    private val callBack: RecycleCallBack
) : RecyclerView.Adapter<RecycleBinAdapter.MyViewHolder>() {

    val listAll: List<RecycleModel> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_notes, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]
        holder.title.text = model.title
        holder.message.text = model.message
        holder.date.text = model.date
        holder.more.setOnClickListener { callBack.chooseNote(model) }
        holder.itemView.setOnClickListener { callBack.chooseNote(model) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.row_notes_title)
        val message: TextView = itemView.findViewById(R.id.row_notes_message)
        val date: TextView = itemView.findViewById(R.id.row_notes_date)
        val priority: ImageView = itemView.findViewById(R.id.row_notes_priority)
        val more: ImageView = itemView.findViewById(R.id.row_notes_moreicon)
        val card: ConstraintLayout = itemView.findViewById(R.id.row_notes_card)
        init {
            priority.visibility = View.INVISIBLE
        }
    }
}