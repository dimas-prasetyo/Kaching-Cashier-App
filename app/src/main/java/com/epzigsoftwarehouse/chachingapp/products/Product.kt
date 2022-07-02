package com.epzigsoftwarehouse.chachingapp.products

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
        var chose_amount: Int)
