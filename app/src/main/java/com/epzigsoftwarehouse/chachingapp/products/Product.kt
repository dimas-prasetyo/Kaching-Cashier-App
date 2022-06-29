package com.epzigsoftwarehouse.chachingapp.products

data class Product(
        val id: Int,
        val category: String,
        val name: String,
        val price: Double,
        val proportion: Double,
        val unit: String,
        val amount: Int,
        val photo_path: String,
        val barcode: String,
        var chose_amount: Int)
