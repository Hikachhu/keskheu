package com.projet

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.projet.R

class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val view: TextView = itemView.findViewById(R.id.randomText)

}

