package com.tanvir.training.actioninputsbatch04

import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.mdasrafulalam78.roomdbdemo.R

@BindingAdapter("app:setFavoriteIcon")
fun setFavoriteIcon(imageView: ImageView, favorite: Boolean) {
    if (favorite) {
        imageView.setImageResource(R.drawable.ic_baseline_favorite_24_red)
    } else {
        imageView.setImageResource(R.drawable.ic_baseline_favorite_24_grey)
    }
}
//@BindingAdapter("app:setImageResources")
//fun setImageResources(imageView: ImageView, url:String){
//    if (url!=null){
//        imageView.setImageBitmap(BitmapFactory.decodeFile(url))
//    }
//}