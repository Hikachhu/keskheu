package com.keskheu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keskheu.api.Recuperation
import com.keskheu.recyclerView.listPM.AdapterListPM
import okhttp3.internal.notify


class ConnecteFragment : Fragment() {

    private var systemApi= Recuperation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_connecte, container, false)
        val usernameConnecte=root.findViewById(R.id.usernameDisplay) as TextView
        var Account2=root.findViewById(R.id.account2) as EditText
        var textMessage=root.findViewById(R.id.textMessage) as EditText
        var boutonEnvoiMessage=root.findViewById(R.id.boutonEnvoiPM) as Button
        val recyclerView:RecyclerView=root.findViewById(R.id.recyclerviewListPM)
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        val listPrivateMessage=systemApi.requestListPM(IdUser)
        Log.e("Apres api",listPrivateMessage.toString())
        val adapter =context?.applicationContext?.let { AdapterListPM(it,listPrivateMessage) }
        recyclerView.adapter = adapter
        val resId: Int =R.anim.layout_animation_fall_down
        val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation( context, resId)
        recyclerView.layoutAnimation = animation
        recyclerView.adapter?.notifyDataSetChanged()
        usernameConnecte.text= USERNAME
        boutonEnvoiMessage.setOnClickListener {
            view?.let { it1 ->
                systemApi.insertPM(Account2.text.toString(),IdUser,textMessage.text.toString(),
                    it1
                )
            }
        }
        return root
    }

    private fun changeScreen(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace((requireView().parent as ViewGroup).id, fragment)
            .addToBackStack(null)
            .commit()
    }
}