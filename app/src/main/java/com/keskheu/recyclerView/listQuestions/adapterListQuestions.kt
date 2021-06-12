package com.keskheu.recyclerView.listQuestions

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
import com.keskheu.api.Question
import com.keskheu.database.AccesLocal


class adapterListQuestions(context: Context) : RecyclerView.Adapter<RecyclerViewHolderListQuestion>() {

    private var accesLocal = AccesLocal(context)
    var parentActuel=0
    var enfantActuel=0
    var positionnement=0
    var Etat=0

    override fun getItemViewType(position: Int): Int {
        return R.layout.frame_textview_listquestions
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolderListQuestion {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return RecyclerViewHolderListQuestion(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerViewHolderListQuestion, position: Int) {
        val animation1 = AlphaAnimation(0.2f, 1.0f)
        animation1.duration = 1000
        animation1.startOffset = 1000
        animation1.fillAfter = true
        if(Etat==0) {
            holder.view.text = accesLocal.listeFils(0)[position].Contenu.toString()+"\nQuestion posée par: "+accesLocal.listeFils(0)[position].Username.toString()+"\nIl y a "+accesLocal.nombreDeRep( accesLocal.listeFils(0)[position].Fils)+" réponses disponibles"
            Log.e("Affichage","Affichage de "+accesLocal.listeFils(0)[position].Contenu.toString())
        }
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