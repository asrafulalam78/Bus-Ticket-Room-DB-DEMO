package com.mdasrafulalam78.roomdbdemo

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdasrafulalam78.roomdbdemo.adapter.CartAdapter
import com.mdasrafulalam78.roomdbdemo.adapter.FavouritesAdapter
import com.mdasrafulalam78.roomdbdemo.adapter.RowAction
import com.mdasrafulalam78.roomdbdemo.databinding.FragmentCartListBinding
import com.mdasrafulalam78.roomdbdemo.databinding.FragmentFavouriteListBinding
import com.mdasrafulalam78.roomdbdemo.model.BusSchedule
import com.mdasrafulalam78.roomdbdemo.model.Cart
import com.mdasrafulalam78.roomdbdemo.viewmodel.ScheduleViewModel
import kotlinx.coroutines.launch

class CartListFragment : Fragment() {
    private lateinit var preference: LoginPreference
    private var _binding: FragmentCartListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<ScheduleViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu,menu)
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