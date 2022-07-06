package com.epzigsoftwarehouse.chachingapp.history


data class History(
    var id: Int,
    var transaction_id: String,
    var date: String,
    var time: String,
    var product_id: Int,
    var product_name: String,
    var price: Double,
    var amount: Int
)
