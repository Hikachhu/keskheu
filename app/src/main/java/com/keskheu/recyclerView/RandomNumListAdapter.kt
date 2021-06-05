package com.keskheu.recyclerView

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.keskheu.database.AccesLocal
import com.keskheu.api.Question
import com.keskheu.R


class RandomNumListAdapter(context: Context) : RecyclerView.Adapter<RecyclerViewHolder>() {

    private var accesLocal = AccesLocal(context)
    var parentActuel=0
    var enfantActuel=0
    var positionnement=0
    var Etat=0

    override fun getItemViewType(position: Int): Int {
        return R.layout.frame_textview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return RecyclerViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        if(Etat==0) holder.view.text = accesLocal.listeFils(0)[position].Contenu.toString()+"\nQuestion posée par: "+accesLocal.listeFils(0)[position].Username.toString()+"\nIl y a "+accesLocal.nombreDeRep( accesLocal.listeFils(0)[position].Fils)+" réponses disponibles"
        else {
            enfantActuel=accesLocal.numFils(parentActuel,positionnement+1)
            Log.e("onBindViewHolder","On cherche les enfants de "+accesLocal.numFils(parentActuel,positionnement+1))
            val Result: ArrayList<Question> =accesLocal.listeFils(accesLocal.numFils(parentActuel,positionnement+1))
            Log.e("onBindViewHolder","Result taille:"+Result.size+" position="+position)
            holder.view.text = Result[position].Contenu.toString()+"\nQuestion posée par: "+Result[position].Username.toString()+"\nIl y a "+accesLocal.nombreDeRep(Result[position].Fils).toString()+" réponses disponibles"
        }
    }

    override fun getItemCount(): Int {
        val valeur:Int=accesLocal.getNumber(accesLocal.numFils(parentActuel,positionnement+1))
        Log.e("Nombre Taille:","taille="+valeur+" Etat:"+Etat)
        return if(Etat==0) accesLocal.getNumber(0)
        else accesLocal.getNumber(accesLocal.numFils(parentActuel,positionnement+1))
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