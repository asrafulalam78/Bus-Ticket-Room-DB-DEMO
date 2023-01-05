package com.mdasrafulalam78.roomdbdemo

import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mdasrafulalam78.roomdbdemo.adapter.FavouritesAdapter
import com.mdasrafulalam78.roomdbdemo.adapter.RowAction
import com.mdasrafulalam78.roomdbdemo.adapter.RowAction.*
import com.mdasrafulalam78.roomdbdemo.customdialogs.CustomAlertDialog
import com.mdasrafulalam78.roomdbdemo.databinding.FragmentFavouriteListBinding
import com.mdasrafulalam78.roomdbdemo.model.BusSchedule
import com.mdasrafulalam78.roomdbdemo.viewmodel.ScheduleViewModel


class FavouriteListFragment : Fragment() {
    private var _binding: FragmentFavouriteListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<ScheduleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavouriteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val favouritesAdapter = FavouritesAdapter(::onMenuItemClicked, ::updateFavorite)
        binding.favouriteRV.layoutManager = LinearLayoutManager(requireActivity())
        binding.favouriteRV.adapter = favouritesAdapter
        viewModel.getScheduleByFavourite().observe(viewLifecycleOwner, Observer {
            favouritesAdapter.submitList(it)
        })

    }
    private fun updateFavorite(schedule: BusSchedule) {
        viewModel.updateSchedule(schedule)
    }
    private fun onMenuItemClicked(schedule: BusSchedule, action: RowAction) {
        when(action) {
            ADD_TO_CART -> {

            }
            PURCHASE_NOW -> {
                TODO()
            }
            else -> {}
        }
    }

}