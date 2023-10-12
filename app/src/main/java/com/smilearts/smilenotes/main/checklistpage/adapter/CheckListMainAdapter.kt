package com.smilearts.smilenotes.main.checklistpage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.smilearts.smilenotes.R
import com.smilearts.smilenotes.main.callback.CheckListCallBack
import com.smilearts.smilenotes.model.CheckListModel
import com.smilearts.smilenotes.model.NotesModel
import com.smilearts.smilenotes.util.AppUtil
import java.util.Locale

class CheckListMainAdapter(
    private var checkList: ArrayList<CheckListModel>,
    private val callBack: CheckListCallBack
) : RecyclerView.Adapter<CheckListMainAdapter.MyViewHolder>(), Filterable {

    private val listAll = checkList

    fun updateDetails(updateList: ArrayList<CheckListModel>) {
        checkList = updateList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_check_list_main, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val checkData = checkList[position]
        holder.title.text = checkData.title
        holder.date.text = checkData.date

        if (checkData.priority == 0) {
            holder.priority.setImageResource(R.drawable.non_priority_icon)
        } else {
            holder.priority.setImageResource(R.drawable.priority_icon)
        }
        holder.more.setImageResource(R.drawable.more_icon1)
        when (checkData.bg) {
            AppUtil.DEFAULT -> {
                holder.card.setBackgroundResource(R.color.DEFAULT)
                holder.cardView.strokeWidth = 3
            }
            AppUtil.LIGHTTHEME -> {
                holder.cardView.strokeWidth = 0
                holder.card.setBackgroundResource(R.color.LIGHTTHEME)
            }
            AppUtil.DARKYELLOW -> {
                holder.cardView.strokeWidth = 0
                holder.card.setBackgroundResource(R.color.DARKYELLOW)
            }
            AppUtil.LIGHTBLUE -> {
                holder.cardView.strokeWidth = 0
                holder.card.setBackgroundResource(R.color.LIGHTBLUE)
            }
            AppUtil.LIGHTGREEN -> {
                holder.cardView.strokeWidth = 0
                holder.card.setBackgroundResource(R.color.LIGHTGREEN)
            }
            AppUtil.LIGHTYELLOW -> {
                holder.cardView.strokeWidth = 0
                holder.card.setBackgroundResource(R.color.LIGHTYELLOW)
            }
        }

        val adapter = CheckListRowAdapter(checkData.point)
        holder.points.setHasFixedSize(true)
        holder.points.adapter = adapter

        holder.more.setOnClickListener {
            callBack.optionMenu(checkData)
        }

        holder.cardView.setOnClickListener {
            callBack.setChooseList(checkData)
        }

    }

    override fun getItemCount(): Int {
        return checkList.size
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var filterList: ArrayList<CheckListModel> = ArrayList()
            if (constraint.toString().isEmpty()) {
                filterList.addAll(listAll)
            } else {
                for (note in listAll) {
                    if (note.title.lowercase(Locale.getDefault()).contains(constraint.toString().lowercase(
                            Locale.getDefault()))) {
                        filterList.add(note)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filterList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            checkList = results.values as ArrayList<CheckListModel>
            notifyDataSetChanged()
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.row_check_list_main_title)
        val points: RecyclerView = itemView.findViewById(R.id.row_check_list_listview)
        val date: TextView = itemView.findViewById(R.id.row_check_list_date_time)
        val priority: ImageView = itemView.findViewById(R.id.row_check_list_priority_icon)
        val more: ImageView = itemView.findViewById(R.id.row_check_list_more_icon)
        val card: ConstraintLayout = itemView.findViewById(R.id.row_check_list_card)
        val cardView: MaterialCardView = itemView.findViewById(R.id.row_check_list_card_view)
    }

}