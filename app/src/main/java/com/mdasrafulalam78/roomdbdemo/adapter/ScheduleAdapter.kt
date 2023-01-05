package com.mdasrafulalam78.roomdbdemo.adapter

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdasrafulalam78.roomdbdemo.NewScheduleFragment
import com.mdasrafulalam78.roomdbdemo.R
import com.mdasrafulalam78.roomdbdemo.databinding.ScheduleRowBinding
import com.mdasrafulalam78.roomdbdemo.model.BusSchedule
import com.mdasrafulalam78.roomdbdemo.model.Cart
import java.io.File


class ScheduleAdapter(
    val menuItemCallback: (Cart, RowAction) -> Unit,
    val favoriteCallback: (BusSchedule) -> Unit
) :
    ListAdapter<BusSchedule, ScheduleAdapter.ScheduleViewHolder>(ScheduleDiffUtil()){

    class ScheduleViewHolder(val binding: ScheduleRowBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(busSchedule: BusSchedule) {
                    binding.schedule = busSchedule
                }
            }

    class ScheduleDiffUtil : DiffUtil.ItemCallback<BusSchedule>() {
        override fun areItemsTheSame(oldItem: BusSchedule, newItem: BusSchedule): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BusSchedule, newItem: BusSchedule): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ScheduleRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule: BusSchedule = getItem(position)
        val cart = Cart(0, schedule.name,schedule.from, schedule.to, schedule.departureDate, schedule.departureTime, schedule.busType, schedule.image,schedule.fare)
        holder.bind(schedule)
        holder.binding.rowFavorite.setOnClickListener {
            schedule.favorite = !schedule.favorite
            holder.bind(schedule)
            favoriteCallback(schedule)
        }
        if (schedule.image!=null){
            val file = File(schedule.image)
            if(file.exists()){
                val imgBitmap = BitmapFactory.decodeFile(file.absolutePath)
                Log.d("uri", schedule.image.toString())
//            holder.binding.rowBusImageIV.setImageURI(Uri.parse(schedule.image))
                holder.binding.rowBusImageIV.setImageBitmap(imgBitmap)
            }
        }
        val menuIV = holder.binding.menuIV
        menuIV.setOnClickListener {
            val popupMenu = PopupMenu(menuIV.context, menuIV)
            popupMenu.menuInflater.inflate(R.menu.row_menu, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {menuItem ->
                val action: RowAction = when(menuItem.itemId) {
                    R.id.item_edit -> RowAction.ADD_TO_CART
                    R.id.item_delete -> RowAction.PURCHASE_NOW
                    else -> RowAction.NONE
                }
                menuItemCallback(cart, action)
                true
            }
        }
    }
}

enum class RowAction {
    EDIT, DELETE, NONE, ADD_TO_CART, PURCHASE_NOW
}

