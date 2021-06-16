package com.keskheu

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keskheu.api.Recuperation
import com.keskheu.recyclerView.listContact.AdapterListContact
import com.keskheu.recyclerView.listPM.AdapterListPM
import com.keskheu.recyclerView.listQuestions.ItemClickSupportListQuestions
import com.keskheu.recyclerView.listQuestions.adapterListQuestions
import com.keskheu.screens.home.HomeFragment


class ConnecteFragment : Fragment() {

    private var systemApi= Recuperation
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    changeScreen(DirectionConnection())
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        val root=inflater.inflate(R.layout.fragment_connecte, container, false)
        val usernameConnecte=root.findViewById(R.id.usernameDisplay) as TextView
        val textMessage=root.findViewById(R.id.textMessage) as EditText
        val boutonEnvoiMessage=root.findViewById(R.id.boutonEnvoiPM) as Button
        val recyclerView:RecyclerView=root.findViewById(R.id.recyclerviewListPM)
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        val listPrivateMessage=systemApi.listAccount()
        Log.e("Apres api",listPrivateMessage.toString())
        val adapter =context?.applicationContext?.let { AdapterListContact(it,listPrivateMessage) }
        recyclerView.adapter = adapter
        val resId: Int =R.anim.layout_animation_fall_down
        val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation( context, resId)
        recyclerView.layoutAnimation = animation
        this.configureOnClickRecyclerView(recyclerView,root)
        usernameConnecte.text= USERNAME
        var account2="ERROR"
        val spinnerRegion = root.findViewById(R.id.listDestinataire) as Spinner
        val lRegion = Recuperation.listAccount()
        val dataAdapterR: ArrayAdapter<String>? =
            context?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, lRegion) }
        dataAdapterR?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRegion.adapter = dataAdapterR
        spinnerRegion.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                account2=lRegion[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        boutonEnvoiMessage.setOnClickListener {

            view?.let { it1 ->
                systemApi.insertPM(account2,IdUser,textMessage.text.toString(),
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

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun configureOnClickRecyclerView(recyclerView: RecyclerView,root:View) {
        ItemClickSupportListQuestions.addTo(recyclerView, R.layout.frame_textview_listquestions).setOnItemClickListener { recyclerView, position, _ ->
            var list = Recuperation.requestListPM(IdUser, position+1)
            val adapter =context?.let { AdapterListPM(it,list) }
            recyclerView.adapter = adapter
            val resId: Int =R.anim.layout_animation_fall_down
            val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation( context, resId)
            recyclerView.layoutAnimation = animation
            Log.e("Change screen","Nouveau screen=$list")

            val callback: OnBackPressedCallback =
                object : OnBackPressedCallback(true /* enabled by default */) {
                    override fun handleOnBackPressed() {
                        val recyclerView:RecyclerView=root.findViewById(R.id.recyclerviewListPM)
                        recyclerView.layoutManager = LinearLayoutManager(root.context)
                        val listPrivateMessage=systemApi.listAccount()
                        Log.e("Apres api",listPrivateMessage.toString())
                        val adapter =context?.applicationContext?.let { AdapterListContact(it,listPrivateMessage) }
                        recyclerView.adapter = adapter
                        val resId: Int =R.anim.layout_animation_fall_down
                        val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation( context, resId)
                        recyclerView.layoutAnimation = animation


                        val callback: OnBackPressedCallback =
                            object : OnBackPressedCallback(true /* enabled by default */) {
                                override fun handleOnBackPressed() {
                                    changeScreen(DirectionConnection())
                                }
                            }
                        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


                    }
                }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        }
    }
}