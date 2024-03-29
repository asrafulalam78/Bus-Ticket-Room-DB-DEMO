package com.mdasrafulalam78.roomdbdemo

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.android.material.snackbar.Snackbar
import com.mdasrafulalam78.roomdbdemo.adapter.RowAction
import com.mdasrafulalam78.roomdbdemo.adapter.ScheduleAdapter
import com.mdasrafulalam78.roomdbdemo.customdialogs.CustomAlertDialog
import com.mdasrafulalam78.roomdbdemo.databinding.FragmentScheduleListBinding
import com.mdasrafulalam78.roomdbdemo.model.BusSchedule
import com.mdasrafulalam78.roomdbdemo.model.Cart
import com.mdasrafulalam78.roomdbdemo.viewmodel.ScheduleViewModel
import kotlinx.coroutines.launch

class ScheduleListFragment : Fragment()
{
    private lateinit var preference: LoginPreference
    private var userId: Long = 0L
    private lateinit var binding: FragmentScheduleListBinding
    private lateinit var scheduleAdapter: ScheduleAdapter
    private val viewModel: ScheduleViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preference = LoginPreference(requireContext())
        preference.isLoggedInFlow
            .asLiveData()
            .observe(viewLifecycleOwner){
                if (!it) {
                    findNavController().navigate(R.id.action_schedulListFragment_to_loginFragment)
                }
            }
        preference.userIdFlow.asLiveData().observe(viewLifecycleOwner, Observer {
            userId = it
        })
        binding = FragmentScheduleListBinding.inflate(inflater, container, false)
        scheduleAdapter = ScheduleAdapter(::onMenuItemClicked, ::updateFavorite)
        binding.scheduleRV.layoutManager = LinearLayoutManager(requireActivity())
        binding.scheduleRV.adapter = scheduleAdapter

        viewModel.getAllSchedules().observe(viewLifecycleOwner) {scheduleList ->
            scheduleAdapter.submitList(scheduleList)
        }
        binding.scheduleRV.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.e("ScheduleListFragment", "dx: $dx, dy: $dy")
                if (dy > 0) {
                    binding.fab.hide()
                }
                if (dy < 0) {
                    binding.fab.show()
                }
            }
        })
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_schedulListFragment_to_newScheduleFragment)
        }
        return binding.root
    }

    companion object{
         var userId: Long = 0L
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu,menu)
        val search = menu?.findItem(R.id.action_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                var scheduleListMain: List<BusSchedule>
                viewModel.getAllSchedules().observe(viewLifecycleOwner, Observer {
                     scheduleListMain = it
                    var collectionSearch: List<BusSchedule> = scheduleListMain.filter {
                        it.name.contains(query.toString())
                    }.toList()
                    scheduleAdapter.submitList(collectionSearch)
                })

                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                var scheduleListMain: List<BusSchedule>
                viewModel.getAllSchedules().observe(viewLifecycleOwner, Observer {
                    scheduleListMain = it
                    var collectionSearch: List<BusSchedule> = scheduleListMain.filter {
                        it.name.contains(newText.toString())
                    }.toList()
                    scheduleAdapter.submitList(collectionSearch)
                })
                return true
            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_notification -> Toast.makeText(requireContext(),"No new notification", Toast.LENGTH_SHORT).show()
            R.id.action_message -> Toast.makeText(requireContext(),"No new messsage", Toast.LENGTH_SHORT).show()
            R.id.action_search -> Toast.makeText(requireContext(),"Search is not implemented yet", Toast.LENGTH_SHORT).show()
            R.id.logout -> {
                preference.userIdFlow.asLiveData().observe(this, Observer {
                    ScheduleListFragment.userId = it
                })
                lifecycle.coroutineScope.launch {
                    preference.setLoginStatus(false, ScheduleListFragment.userId, requireContext())
                }
            }
        }
        return super.onOptionsItemSelected(item)
//                Toast.makeText(this,"Your Profile will be visible soon!",Toast.LENGTH_SHORT).show()
    }

    private fun updateFavorite(schedule: BusSchedule) {
        viewModel.updateSchedule(schedule)
    }

    private fun onMenuItemClicked(cart: Cart, action: RowAction){
        when(action){
            RowAction.ADD_TO_CART -> {
                viewModel.addToCart(cart)
            }
            RowAction.PURCHASE_NOW->{
                Toast.makeText(requireContext(), "This Feature will available soon!", Toast.LENGTH_SHORT).show()}
            else -> {}
        }
    }

}