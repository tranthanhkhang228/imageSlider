package com.example.getimagesfromjson

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide


class ImageSliderAdapter(private val urls: Array<String>) : PagerAdapter() {
    override fun getCount(): Int {
        return urls.size
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // Tạo ImageView
        val context: Context = container.context
        val imageView = AppCompatImageView(context)
        container.addView(imageView)
        // Load ảnh vào ImageView bằng Glide
        Glide.with(context).load(urls[position]).into(imageView)
        // Return
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // container chính là ViewPager, còn Object chính là return của instantiateItem ứng với position
        container.removeView(`object` as View)
    }

}