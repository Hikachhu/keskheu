package com.keskheu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.keskheu.screens.connection.ConnectionFragment
import com.keskheu.screens.home.HomeFragment

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DirectionConnection : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        Log.e("New Screen","Debut Direction connection")
        val root=inflater.inflate(R.layout.fragment_direction_connection, container, false)
        val accesCompte=root.findViewById(R.id.accesCompte) as Button
        val seConnecter=root.findViewById(R.id.seConnecter) as Button
        accesCompte.setOnClickListener {
            changeScreen(ConnecteFragment())
        }
        seConnecter.setOnClickListener {
            changeScreen(ConnectionFragment())
        }
        return root
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            DirectionConnection().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun changeScreen(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace((requireView().parent as ViewGroup).id, fragment)
            .addToBackStack(null)
            .commit()
    }
}