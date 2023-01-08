package com.mdasrafulalam78.roomdbdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.mdasrafulalam78.roomdbdemo.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var preference: LoginPreference
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preference = LoginPreference(applicationContext)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        val search = menu?.findItem(R.id.action_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
//                adapter.filter.filter(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_notification -> Toast.makeText(this,"No new notification", Toast.LENGTH_SHORT).show()
            R.id.action_message -> Toast.makeText(this,"No new messsage", Toast.LENGTH_SHORT).show()
            R.id.action_search -> Toast.makeText(this,"Search is not implemented yet", Toast.LENGTH_SHORT).show()
            R.id.logout -> {
                preference.userIdFlow.asLiveData().observe(this, Observer {
                    ScheduleListFragment.userId = it
                })
                lifecycle.coroutineScope.launch {
                    preference.setLoginStatus(false, ScheduleListFragment.userId, this@MainActivity)
                }
            }
        }
        return super.onOptionsItemSelected(item)
//                Toast.makeText(this,"Your Profile will be visible soon!",Toast.LENGTH_SHORT).show()
        }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}