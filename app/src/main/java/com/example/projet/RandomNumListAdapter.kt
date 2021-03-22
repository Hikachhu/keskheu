package com.example.projet

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar


class RandomNumListAdapter(context: Context) : RecyclerView.Adapter<RecyclerViewHolder>() {

    private var accesLocal = AccesLocal(context)
    private var checkedPosition = 0
    var parentActuel=0;
    var enfantActuel=0;
    var positionnement=0;
    var Etat=0;

    override fun getItemViewType(position: Int): Int {
        return R.layout.frame_textview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        if(Etat==0) holder.view.text = accesLocal.ListeFils(0)[position].Contenu.toString()+"\t par "+accesLocal.ListeFils(0)[position].Username.toString()
        else {
            enfantActuel=accesLocal.NumFils(parentActuel,positionnement+1)
            Log.e("onBindViewHolder","On cherche les enfants de "+accesLocal.NumFils(parentActuel,positionnement+1))
            var Result: ArrayList<Question> =accesLocal.ListeFils(accesLocal.NumFils(parentActuel,positionnement+1))
            Log.e("onBindViewHolder","Result taille:"+Result.size+" position="+position)
            holder.view.text = Result[position].Contenu.toString()+" de "+Result[position].Username.toString()
        }
    }

    override fun getItemCount(): Int {
        var valeur:Int=accesLocal.getNumber(accesLocal.NumFils(parentActuel,positionnement+1))
        Log.e("Nombre Taille:","taille="+valeur+" Etat:"+Etat)
        return if(Etat==0) accesLocal.getNumber(0)
        else accesLocal.getNumber(accesLocal.NumFils(parentActuel,positionnement+1));
    }

    class ViewHolder(itemView: View, onNoteListener: OnNoteListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var mOnNoteListener: OnNoteListener = onNoteListener
        override fun onClick(view: View?) {
            mOnNoteListener.onNoteClick(adapterPosition);
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