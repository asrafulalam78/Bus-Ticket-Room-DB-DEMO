package com.mdasrafulalam78.roomdbdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdasrafulalam78.roomdbdemo.adapter.CartAdapter
import com.mdasrafulalam78.roomdbdemo.adapter.FavouritesAdapter
import com.mdasrafulalam78.roomdbdemo.adapter.RowAction
import com.mdasrafulalam78.roomdbdemo.databinding.FragmentCartListBinding
import com.mdasrafulalam78.roomdbdemo.databinding.FragmentFavouriteListBinding
import com.mdasrafulalam78.roomdbdemo.model.BusSchedule
import com.mdasrafulalam78.roomdbdemo.model.Cart
import com.mdasrafulalam78.roomdbdemo.viewmodel.ScheduleViewModel

class CartListFragment : Fragment() {
    private var _binding: FragmentCartListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<ScheduleViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCartListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cartAdapter = CartAdapter(::onMenuItemClicked)
        binding.cartRV.layoutManager = LinearLayoutManager(requireActivity())
        binding.cartRV.adapter = cartAdapter
        viewModel.getAllCarts().observe(viewLifecycleOwner, Observer {
            cartAdapter.submitList(it)
        })
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