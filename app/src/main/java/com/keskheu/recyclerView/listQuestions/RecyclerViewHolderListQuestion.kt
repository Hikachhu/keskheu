package com.keskheu.recyclerView.listQuestions

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keskheu.R

class RecyclerViewHolderListQuestion(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val view: TextView = itemView.findViewById(R.id.randomText_listQuestions)

}

