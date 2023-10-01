package com.reviwh.githubuser

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context).load(url).circleCrop().into(this)
}