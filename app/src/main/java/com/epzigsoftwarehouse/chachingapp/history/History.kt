package com.epzigsoftwarehouse.chachingapp.history


data class History(
    var id: Int,
    var transaction_id: String,
    var date: String,
    var time: String,
    var cashier_id: Int,
    var product_id: Int,
    var product_name: String,
    var price: Double,
    var tax: Double,
    var tip: Double,
    var cash: Double,
    var amount: Int
)
