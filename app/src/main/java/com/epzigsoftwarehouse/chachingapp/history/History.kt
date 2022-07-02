package com.epzigsoftwarehouse.chachingapp.history

import java.sql.Time
import java.util.*

data class History(
    var id: Int,
    var transaction_id: String,
    var date: Date,
    var time: Time,
    var product_name: String,
    var price: Double,
    var amount: Int
)
