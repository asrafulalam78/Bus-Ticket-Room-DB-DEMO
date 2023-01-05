package com.mdasrafulalam78.roomdbdemo.adapter

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdasrafulalam78.roomdbdemo.databinding.CartRowBinding
import com.mdasrafulalam78.roomdbdemo.model.Cart

class CartAdapter(val menuItemCallback: (Cart, RowAction) -> Unit):
    ListAdapter<Cart, CartAdapter.CartViewHolder>(CartDiffUtil()){
    private var count = 1;
    class CartViewHolder(val binding: CartRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: Cart) {
            binding.cartItem = cart
        }
    }

    class CartDiffUtil : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem: Cart = getItem(position)
        holder.bind(cartItem)
        if (cartItem.image!=null){
            holder.binding.cartRowBusImage.setImageBitmap(BitmapFactory.decodeFile(cartItem.image))
            holder.binding.cartRowBusImage.setImageURI(Uri.parse(cartItem.image))
//            Log.d("image", schedule.image.toString())
        }
        holder.binding.addIV.setOnClickListener(View.OnClickListener {
            count++
            holder.binding.cartRowCountTV.text = count.toString()
        })
        holder.binding.removeIV.setOnClickListener(View.OnClickListener {
            if (count>=2){
                count--
                holder.binding.cartRowCountTV.text = count.toString()
            }
        })
    }
}