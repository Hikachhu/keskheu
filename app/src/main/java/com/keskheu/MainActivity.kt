package com.keskheu

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


var USERNAME:String="Unconnected"

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var nomUser: TextView
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.fragment_ouverture) //HERE

        val ouverture:Button= findViewById(R.id.buttonAccesAppli)
        ouverture.setOnClickListener {

            this.setContentView(R.layout.activity_main)
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)
            val fab: FloatingActionButton = findViewById(R.id.fab)
            fab.setOnClickListener { view ->
                Snackbar.make(
                    view,
                    "Version->[" + BuildConfig.VERSION_NAME + "] User=" + USERNAME,
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null).show()
            }

            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            val navView: NavigationView = findViewById(R.id.nav_view)
            val navController = findNavController(R.id.nav_host_fragment)
            val headerView: View = navView.getHeaderView(0)
            nomUser = headerView.findViewById(R.id.NomUtilisateur)
            appBarConfiguration =
                AppBarConfiguration(setOf(R.id.nav_home, R.id.nav_slideshow), drawerLayout)
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun changeUsername(){
       nomUser.text= USERNAME
    }


}