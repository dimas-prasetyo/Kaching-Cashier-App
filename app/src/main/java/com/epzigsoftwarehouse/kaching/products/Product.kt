package com.epzigsoftwarehouse.kaching.products

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
        var id: Int,
        var category: String,
        var name: String,
        var price: Double,
        var proportion: Double,
        var unit: String,
        var amount: Int,
        var photo_path: String,
        var barcode: String,
        var chose_amount: Int): Parcelable
