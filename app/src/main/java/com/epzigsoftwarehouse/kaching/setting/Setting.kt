package com.epzigsoftwarehouse.kaching.setting

data class Setting(
    var id: Int,
    var logo_path: String,
    var store_name: String,
    var cashier_active: Int,
    var language: String,
    var currency: String
    )
