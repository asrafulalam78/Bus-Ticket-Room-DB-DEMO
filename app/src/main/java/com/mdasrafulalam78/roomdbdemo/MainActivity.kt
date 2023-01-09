package com.mdasrafulalam78.roomdbdemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mdasrafulalam78.roomdbdemo.R
import com.mdasrafulalam78.roomdbdemo.adapter.ScheduleAdapter
import com.mdasrafulalam78.roomdbdemo.databinding.FragmentScheduleListBinding
import com.mdasrafulalam78.roomdbdemo.model.BusSchedule
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var preference: LoginPreference
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        preference = LoginPreference(applicationContext)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener{_, nd: NavDestination,_ ->
            if (nd.id == R.id.loginFragment){
                navView.visibility = View.GONE
            }else{
                navView.visibility = View.VISIBLE
            }
        }
//        bottomNavigationView.itemIconTintList = null
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.schedulListFragment-> {navController.navigate(R.id.schedulListFragment)
                    true
                }
                R.id.favouriteListFragment-> {navController.navigate(R.id.favouriteListFragment)
                    true
                }
                R.id.cartListFragment-> {navController.navigate(R.id.cartListFragment)
                    true
                }
                else -> {false}
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}