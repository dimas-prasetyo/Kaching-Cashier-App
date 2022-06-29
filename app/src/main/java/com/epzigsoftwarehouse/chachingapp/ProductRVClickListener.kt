package com.epzigsoftwarehouse.chachingapp

import android.view.View
import com.epzigsoftwarehouse.chachingapp.products.Product

interface ProductRVClickListener {
    fun onItemClicked(view: View, product: Product)
}
