package com.keskheu.screens.connection

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.keskheu.*
import com.keskheu.Utils.toMD5
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import com.keskheu.api.Recuperation
import com.keskheu.screens.home.HomeFragment


class ConnectionFragment : Fragment() {

    private lateinit var connectionViewModel: ConnectionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        connectionViewModel = ViewModelProvider(this).get(ConnectionViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    changeScreen(DirectionConnection())
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        val textView: TextView = root.findViewById(R.id.Text_informatif)
        connectionViewModel.lu.observe(viewLifecycleOwner, {
            textView.text = it
        })

        val username = root.findViewById<EditText>(R.id.Username)
        val mdp = root.findViewById<EditText>(R.id.Mdp)

        val creationAccount = root.findViewById<Button>(R.id.Creation)
        val connectionAccount = root.findViewById<Button>(R.id.Connection)

        connectionAccount.setOnClickListener {
            val nomUtilisateur = username.text.toString().trim()
            val motdepasse = mdp.text.toString().trim().toMD5()
            val registrationForm1 = JSONObject()
            try {
                registrationForm1.put("subject", "Connection")
                registrationForm1.put("Username", nomUtilisateur)
                registrationForm1.put("Mdp", motdepasse)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val body: RequestBody = registrationForm1.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            var retour = 1
            view?.let { it1 ->
                retour = Recuperation.demandeConnection(body, nomUtilisateur, it1)
            }
            Log.e("Code retour", "retour = $retour")
            if (retour == 0) {
                Log.e("AppBar", "Changement comportement")
                try {
                    (activity as MainActivity).findViewById<TextView>(R.id.NomUtilisateur).text=
                        USERNAME
                } catch (e: Exception) {
                    Log.e("ERROR AppBar Changement", e.toString())
                }
            }


        }

        creationAccount.setOnClickListener {
            val nomUtilisateur = username.text.toString().trim()
            val motdepasse = mdp.text.toString().trim().toMD5()
            Log.e("MD5", motdepasse)
            val registrationForm1 = JSONObject()
            try {
                registrationForm1.put("subject", "Creation")
                registrationForm1.put("Username", nomUtilisateur)
                registrationForm1.put("Mdp", motdepasse)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val body: RequestBody = registrationForm1.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            var retour = 1
            view?.let { it1 ->
                retour = Recuperation.demandeConnection(body, nomUtilisateur, it1)
            }
            Log.e("Code retour", "retour = $retour")
            if (retour == 0) {
                Log.e("AppBar", "Changement comportement")
                try {
                    val navView: NavigationView = root.findViewById(R.id.nav_view)
                    val drawerLayout: DrawerLayout = root.findViewById(R.id.drawer_layout)
                    val navController: NavController = findNavController()
                    var appBarConfiguration = AppBarConfiguration(
                        setOf(R.id.nav_home, R.id.nav_Connecte),
                        drawerLayout
                    )
                    (activity as MainActivity).setupActionBarWithNavController(
                        navController,
                        appBarConfiguration
                    )
                    navView.setupWithNavController(navController)
                } catch (e: Exception) {
                    Log.e("ERROR AppBar Changement", e.toString())
                }
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