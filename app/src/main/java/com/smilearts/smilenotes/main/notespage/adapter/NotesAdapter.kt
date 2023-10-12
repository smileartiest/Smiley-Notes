package com.smilearts.smilenotes.main.notespage.adapter

import android.annotation.SuppressLint
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
import com.smilearts.smilenotes.main.callback.NotesCallBack
import com.smilearts.smilenotes.model.NotesModel
import com.smilearts.smilenotes.util.AppUtil
import java.util.Locale

@SuppressLint("NotifyDataSetChanged")
class NotesAdapter(
    private var list: ArrayList<NotesModel>,
    private val callBack: NotesCallBack
) : RecyclerView.Adapter<NotesAdapter.MyViewHolder>(), Filterable {

    private val listAll = list
    private var chooseState = false

    fun updateDetails(updateList: ArrayList<NotesModel>) {
        list = updateList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_notes, parent , false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]
        holder.title.text = model.title
        holder.message.text = model.message
        holder.date.text = model.date
        if (model.priority == 0) {
            holder.priority.setImageResource(R.drawable.non_priority_icon)
        } else {
            holder.priority.setImageResource(R.drawable.priority_icon)
        }
        holder.more.setImageResource(R.drawable.more_icon1)
        when (model.bg) {
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

        holder.cardView.setOnClickListener { v: View? ->
            if (getChooseState()) {
                val status = !holder.cardView.isChecked
                holder.cardView.isChecked = status
                callBack.setChooseList(model)
            } else {
                callBack.moveToNotesPage(model)
            }
        }

        holder.cardView.setOnLongClickListener {
            val status = !holder.cardView.isChecked
            holder.cardView.isChecked = status
            callBack.setChooseList(model)
            true
        }

        holder.more.setOnClickListener { v: View? ->
            callBack.notesOptionScreen(model)
        }
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.row_notes_title)
        val message: TextView = itemView.findViewById(R.id.row_notes_message)
        val date: TextView = itemView.findViewById(R.id.row_notes_date)
        val card: ConstraintLayout = itemView.findViewById(R.id.row_notes_card)
        val priority: ImageView = itemView.findViewById(R.id.row_notes_priority)
        val more: ImageView = itemView.findViewById(R.id.row_notes_moreicon)
        val cardView: MaterialCardView = itemView.findViewById(R.id.row_notes_card_view)
    }

    fun setChooseState(state: Boolean) {
        chooseState = state
    }

    private fun getChooseState(): Boolean {
        return chooseState
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            var filterList: ArrayList<NotesModel> = ArrayList()
            if (constraint.toString().isEmpty()) {
                filterList.addAll(listAll)
            } else {
                for (note in listAll) {
                    if (note.title.lowercase(Locale.getDefault()).contains(constraint.toString().lowercase(Locale.getDefault()))) {
                        filterList.add(note)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filterList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            list = results.values as ArrayList<NotesModel>
            notifyDataSetChanged()
        }
    }

}