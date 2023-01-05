package com.mdasrafulalam78.roomdbdemo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mdasrafulalam78.roomdbdemo.adapter.RowAction
import com.mdasrafulalam78.roomdbdemo.adapter.ScheduleAdapter
import com.mdasrafulalam78.roomdbdemo.customdialogs.CustomAlertDialog
import com.mdasrafulalam78.roomdbdemo.databinding.FragmentScheduleListBinding
import com.mdasrafulalam78.roomdbdemo.model.BusSchedule
import com.mdasrafulalam78.roomdbdemo.model.Cart
import com.mdasrafulalam78.roomdbdemo.viewmodel.ScheduleViewModel

class ScheduleListFragment : Fragment()
{
    private lateinit var binding: FragmentScheduleListBinding
    private val viewModel: ScheduleViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleListBinding.inflate(inflater, container, false)
        val scheduleAdapter = ScheduleAdapter(::onMenuItemClicked, ::updateFavorite)
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