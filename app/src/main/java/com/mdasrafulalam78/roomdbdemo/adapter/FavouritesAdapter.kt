package com.mdasrafulalam78.roomdbdemo.adapter

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdasrafulalam78.roomdbdemo.NewScheduleFragment
import com.mdasrafulalam78.roomdbdemo.R
import com.mdasrafulalam78.roomdbdemo.databinding.FavouriteRowBinding
import com.mdasrafulalam78.roomdbdemo.model.BusSchedule

class FavouritesAdapter(val menuItemCallback: (BusSchedule, RowAction) -> Unit,
                        val favoriteCallback: (BusSchedule) -> Unit):
    ListAdapter<BusSchedule, FavouritesAdapter.FavouriteViewHolder>(FavouriteDiffUtil()) {
    class FavouriteViewHolder(val binding: FavouriteRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(busSchedule: BusSchedule) {
            binding.favSchedule = busSchedule
        }
    }

    class FavouriteDiffUtil : DiffUtil.ItemCallback<BusSchedule>() {
        override fun areItemsTheSame(oldItem: BusSchedule, newItem: BusSchedule): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BusSchedule, newItem: BusSchedule): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteViewHolder {
        val binding = FavouriteRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavouriteViewHolder(binding)
    }


    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val schedule: BusSchedule = getItem(position)
        holder.bind(schedule)
        holder.binding.rowFavorite.setOnClickListener {
            schedule.favorite = !schedule.favorite
            holder.bind(schedule)
            favoriteCallback(schedule)
        }
        if (schedule.image!=null){
            Log.d("uri", schedule.image.toString())
//            holder.binding.rowBusImageIV.setImageURI(Uri.parse(schedule.image))
            holder.binding.rowBusImageIV.setImageBitmap(BitmapFactory.decodeFile(schedule.image))
//            if (NewScheduleFragment.CAMERA){
//                holder.binding.rowBusImageIV.setImageBitmap(BitmapFactory.decodeFile(schedule.image))
//            }else{
//                holder.binding.rowBusImageIV.setImageBitmap(BitmapFactory.decodeFile(schedule.image))
//            }
//            Log.d("image", schedule.image.toString())
        }
        holder.binding.menuIV.setOnClickListener {
            val popupMenu = PopupMenu(holder.binding.menuIV.context, holder.binding.menuIV)
            popupMenu.menuInflater.inflate(R.menu.row_menu, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {menuItem ->
                val action: RowAction = when(menuItem.itemId) {
                    R.id.item_edit -> RowAction.ADD_TO_CART
                    R.id.item_delete -> RowAction.PURCHASE_NOW
                    else -> RowAction.NONE
                }
                menuItemCallback(schedule, action)
                true
            }
        }
    }
}