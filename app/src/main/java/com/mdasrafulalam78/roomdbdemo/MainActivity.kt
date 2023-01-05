package com.mdasrafulalam78.roomdbdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.mdasrafulalam78.roomdbdemo.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
        bottomNavigationView.setupWithNavController(navController)
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