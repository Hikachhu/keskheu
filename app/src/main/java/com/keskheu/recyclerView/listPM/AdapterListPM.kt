package com.keskheu.recyclerView.listPM

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.keskheu.R
import com.keskheu.api.PrivateMessage
import com.keskheu.database.AccesLocal
import java.lang.Exception


class AdapterListPM(context: Context, listPrivateMessage: ArrayList<PrivateMessage>) : RecyclerView.Adapter<RecyclerViewHolderListPM>() {

    private var accesLocal = AccesLocal(context)
    var listPM= listPrivateMessage

    override fun getItemViewType(position: Int): Int {
        return R.layout.frame_textview_listpm
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolderListPM {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return RecyclerViewHolderListPM(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerViewHolderListPM, position: Int) {
        Log.e("Passage",position.toString())
        try {
            val animation1 = AlphaAnimation(0.2f, 1.0f)
            animation1.duration = 1000
            animation1.startOffset = 1000
            animation1.fillAfter = true
            Log.e("onBindViewHolder", listPM[position].toString())
            holder.view.text = listPM[position].textMessage
        }catch (e:Exception){
            Log.e("ERROR",e.toString())
        }

    }

    override fun getItemCount(): Int {
        Log.e("Taille",listPM.size.toString())
        return listPM.size
    }

    class ViewHolder(itemView: View, onNoteListener: OnNoteListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var mOnNoteListener: OnNoteListener = onNoteListener
        override fun onClick(view: View?) {
            mOnNoteListener.onNoteClick(adapterPosition)
            if (view != null) {
                Snackbar.make(view, "Lecture base de donnée, wait", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    interface OnNoteListener {
        fun onNoteClick(position: Int)
    }
}