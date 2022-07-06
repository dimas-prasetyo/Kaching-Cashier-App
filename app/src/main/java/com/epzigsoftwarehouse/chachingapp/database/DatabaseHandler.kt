package com.epzigsoftwarehouse.chachingapp.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.epzigsoftwarehouse.chachingapp.cashier.Cashier
import com.epzigsoftwarehouse.chachingapp.history.History
import com.epzigsoftwarehouse.chachingapp.products.Product
import com.epzigsoftwarehouse.chachingapp.setting.Setting

class DatabaseHandler (context: Context?) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "CashierDatabase"

        private val TABLE_PRODUCTS = "TableProducts"

        private val KEY_ID = "_id"
        private val KEY_CATEGORY = "category"
        private val KEY_NAME = "name"
        private val KEY_PRICE = "price"
        private val KEY_PROPORTION = "proportion"
        private val KEY_UNIT = "unit"
        private val KEY_AMOUNT = "amount"
        private val KEY_PHOTO_PATH= "photo_path"
        private val KEY_BARCODE = "barcode"
        private val KEY_CHOSE_AMOUNT = "chose_amount"

        // Table Cashier
        private val TABLE_CASHIERS = "TableCashiers"

        private val KEY_CASHIER_ID = "_id"
        private val KEY_CASHIER_NAME = "name"
        private val KEY_CASHIER_POSITION = "position"
        private val KEY_CASHIER_PHOTO_PATH= "photo_path"
        private val KEY_CASHIER_CONTACT = "contact"

        // Table History
        private val TABLE_HISTORY = "TableHistory"

        private val KEY_HISTORY_ID = "_id"
        private val KEY_TRANSACTION_ID = "transaction_id"
        private val KEY_TRANSACTION_DATE = "date"
        private val KEY_TRANSACTION_TIME= "time"
        private val KEY_TRANSACTION_PRODUCT_ID = "product_id"
        private val KEY_TRANSACTION_PRODUCT_NAME = "product_name"
        private val KEY_TRANSACTION_PRODUCT_PRICE = "price"
        private val KEY_TRANSACTION_PRODUCT_AMOUNT= "amount"

        // Table Setting
        private val TABLE_SETTING = "TableSetting"

        private val KEY_SETTING_ID = "_id"
        private val KEY_LOGO_PATH = "logo_path"
        private val KEY_STORE_NAME = "store_name"
        private val KEY_CASHIER_ACTIVE= "cashier_active"
        private val KEY_LANGUAGE = "language"
        private val KEY_CURRENCY = "currency"

    }

    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " + TABLE_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY" + " AUTOINCREMENT,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_PRICE + " DOUBLE,"
                + KEY_PROPORTION + " DOUBLE,"
                + KEY_UNIT + " TEXT,"
                + KEY_AMOUNT + " INTEGER,"
                + KEY_PHOTO_PATH + " TEXT,"
                + KEY_BARCODE + " TEXT,"
                + KEY_CHOSE_AMOUNT + " INTEGER" + ")")

        val CREATE_CASHIER_TABLE = ("CREATE TABLE " + TABLE_CASHIERS + "("
                + KEY_CASHIER_ID + " INTEGER PRIMARY KEY" + " AUTOINCREMENT,"
                + KEY_CASHIER_NAME + " TEXT,"
                + KEY_CASHIER_POSITION + " TEXT,"
                + KEY_CASHIER_PHOTO_PATH + " TEXT,"
                + KEY_CASHIER_CONTACT + " TEXT" + ")")

        val CREATE_HISTORY_TABLE = ("CREATE TABLE " + TABLE_HISTORY + "("
                + KEY_HISTORY_ID + " INTEGER PRIMARY KEY" + " AUTOINCREMENT,"
                + KEY_TRANSACTION_ID + " TEXT,"
                + KEY_TRANSACTION_DATE + " TEXT,"
                + KEY_TRANSACTION_TIME + " TEXT,"
                + KEY_TRANSACTION_PRODUCT_ID + " INTEGER,"
                + KEY_TRANSACTION_PRODUCT_NAME + " TEXT,"
                + KEY_TRANSACTION_PRODUCT_PRICE + " DOUBLE,"
                + KEY_TRANSACTION_PRODUCT_AMOUNT + " INTEGER" + ")")


        val CREATE_SETTING_TABLE = ("CREATE TABLE " + TABLE_SETTING + "("
                + KEY_SETTING_ID + " INTEGER PRIMARY KEY" + " AUTOINCREMENT,"
                + KEY_LOGO_PATH + " TEXT,"
                + KEY_STORE_NAME + " TEXT,"
                + KEY_CASHIER_ACTIVE + " INTEGER,"
                + KEY_LANGUAGE + " TEXT,"
                + KEY_CURRENCY + " TEXT" + ")")

        db!!.execSQL(CREATE_PRODUCTS_TABLE)
        db!!.execSQL(CREATE_CASHIER_TABLE)
        db!!.execSQL(CREATE_HISTORY_TABLE)
        db!!.execSQL(CREATE_SETTING_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CASHIERS")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_SETTING")
        onCreate(db)
    }

    //    PRODUCT
    fun addProduct(product: Product): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_CATEGORY, product.category)
        contentValues.put(KEY_NAME, product.name)
        contentValues.put(KEY_PRICE, product.price)
        contentValues.put(KEY_PROPORTION, product.proportion)
        contentValues.put(KEY_UNIT, product.unit)
        contentValues.put(KEY_AMOUNT, product.amount)
        contentValues.put(KEY_PHOTO_PATH, product.photo_path)
        contentValues.put(KEY_BARCODE, product.barcode)
        contentValues.put(KEY_CHOSE_AMOUNT, product.chose_amount)

        val success = db.insert(TABLE_PRODUCTS, null, contentValues)

        db.close()
        return success
    }

    @SuppressLint("Range")
    fun viewAllProduct(): ArrayList<Product> {

        val empList: ArrayList<Product> = ArrayList<Product>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_PRODUCTS"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var category: String
        var name: String
        var price: Double
        var proportion: Double
        var unit: String
        var amount: Int
        var photo_path: String
        var barcode: String
        var chose_amount: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE))
                proportion = cursor.getDouble(cursor.getColumnIndex(KEY_PROPORTION))
                unit = cursor.getString(cursor.getColumnIndex(KEY_UNIT))
                amount = cursor.getInt(cursor.getColumnIndex(KEY_AMOUNT))
                photo_path = cursor.getString(cursor.getColumnIndex(KEY_PHOTO_PATH))
                barcode = cursor.getString(cursor.getColumnIndex(KEY_BARCODE))
                chose_amount = cursor.getInt(cursor.getColumnIndex(KEY_CHOSE_AMOUNT))

                val emp = Product(id = id, category = category, name = name, price = price, proportion = proportion, unit = unit, amount = amount, photo_path = photo_path, barcode = barcode, chose_amount = chose_amount)
                empList.add(emp)

            } while (cursor.moveToNext())
        }
        return empList
    }

    @SuppressLint("Range")
    fun getProduct(_id: Int): Product {
        val product = Product(0,"", "", 0.00, 0.00,"", 0, "", "", 0)
        val db = writableDatabase

        val selectQuery = "SELECT  * FROM $TABLE_PRODUCTS WHERE $KEY_ID == $_id"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    product.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID)))
                    product.category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY))
                    product.name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                    product.price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE))
                    product.proportion = cursor.getDouble(cursor.getColumnIndex(KEY_PROPORTION))
                    product.unit = cursor.getString(cursor.getColumnIndex(KEY_UNIT))
                    product.amount = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_AMOUNT)))
                    product.photo_path = cursor.getString(cursor.getColumnIndex(KEY_PHOTO_PATH))
                    product.barcode = cursor.getString(cursor.getColumnIndex(KEY_BARCODE))
                    product.chose_amount = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CHOSE_AMOUNT)))

                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return product
    }

    fun updateProduct(product: Product): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_CATEGORY, product.category)
        values.put(KEY_NAME, product.name)
        values.put(KEY_PRICE, product.price)
        values.put(KEY_PROPORTION, product.proportion)
        values.put(KEY_UNIT, product.unit)
        values.put(KEY_AMOUNT, product.amount)
        values.put(KEY_PHOTO_PATH, product.photo_path)
        values.put(KEY_BARCODE, product.barcode)
        values.put(KEY_CHOSE_AMOUNT, product.chose_amount)

        val _success = db.update(TABLE_PRODUCTS, values, KEY_ID + "=?", arrayOf(product.id.toString())).toLong()
        db.close()
        //return Integer.parseInt("$_success") != -1
        return _success
    }

    fun deleteProduct(_id: Int): Long {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_PRODUCTS, KEY_ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return _success
    }

    //    CASHIER
    fun addCashier(cashier: Cashier): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_CASHIER_NAME, cashier.name)
        contentValues.put(KEY_CASHIER_POSITION, cashier.position)
        contentValues.put(KEY_CASHIER_PHOTO_PATH, cashier.photo_path)
        contentValues.put(KEY_CASHIER_CONTACT, cashier.contact)

        val success = db.insert(TABLE_CASHIERS, null, contentValues)

        db.close()
        return success
    }

    @SuppressLint("Range")
    fun viewAllCashier(): ArrayList<Cashier> {

        val empList: ArrayList<Cashier> = ArrayList<Cashier>()
        val selectQuery = "SELECT  * FROM $TABLE_CASHIERS"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var position: String
        var photo_path: String
        var contact: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_CASHIER_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_CASHIER_NAME))
                position = cursor.getString(cursor.getColumnIndex(KEY_CASHIER_POSITION))
                photo_path = cursor.getString(cursor.getColumnIndex(KEY_CASHIER_PHOTO_PATH))
                contact = cursor.getString(cursor.getColumnIndex(KEY_CASHIER_CONTACT))

                val emp = Cashier(id = id, name = name, position = position, photo_path = photo_path, contact = contact)
                empList.add(emp)

            } while (cursor.moveToNext())
        }
        return empList
    }

    fun updateCashier(cashier: Cashier): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_CASHIER_NAME, cashier.name)
        values.put(KEY_CASHIER_POSITION, cashier.position)
        values.put(KEY_CASHIER_PHOTO_PATH, cashier.photo_path)
        values.put(KEY_CASHIER_CONTACT, cashier.contact)

        val _success = db.update(TABLE_CASHIERS, values, KEY_ID + "=?", arrayOf(cashier.id.toString())).toLong()
        db.close()
        return _success
    }


    @SuppressLint("Range")
    fun getCashierDetail(_id: Int): Cashier {
        val cashier = Cashier(0,"", "", "", "")
        val db = writableDatabase

        val selectQuery = "SELECT  * FROM $TABLE_CASHIERS WHERE $KEY_CASHIER_ID == $_id"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    cashier.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CASHIER_ID)))
                    cashier.name = cursor.getString(cursor.getColumnIndex(KEY_CASHIER_NAME))
                    cashier.position = cursor.getString(cursor.getColumnIndex(KEY_CASHIER_POSITION))
                    cashier.photo_path = cursor.getString(cursor.getColumnIndex(KEY_CASHIER_PHOTO_PATH))
                    cashier.contact = cursor.getString(cursor.getColumnIndex(KEY_CASHIER_CONTACT))

                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return cashier
    }

    fun deleteCashier(_id: Int): Long {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_CASHIERS, KEY_CASHIER_ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return _success
    }

    //    HISTORY
    fun addHistory(history: History): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TRANSACTION_ID, history.transaction_id)
        contentValues.put(KEY_TRANSACTION_DATE, history.date.toString())
        contentValues.put(KEY_TRANSACTION_TIME, history.time.toString())
        contentValues.put(KEY_TRANSACTION_PRODUCT_ID, history.product_id)
        contentValues.put(KEY_TRANSACTION_PRODUCT_NAME, history.product_name)
        contentValues.put(KEY_TRANSACTION_PRODUCT_PRICE, history.price)
        contentValues.put(KEY_TRANSACTION_PRODUCT_AMOUNT, history.amount)

        val success = db.insert(TABLE_HISTORY, null, contentValues)

        db.close()
        return success
    }

    /*@SuppressLint("Range")
    fun viewAllHistory(): ArrayList<History> {

        val empList: ArrayList<History> = ArrayList<History>()
        val selectQuery = "SELECT  * FROM $TABLE_HISTORY"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var transaction_id: Int
        var date: Date
        var time: Time
        var product_name: String
        var price: Double
        var amount: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_HISTORY_ID))
                transaction_id = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_ID))
                date = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_DATE))
                time = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_TIME))
                product_name = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_PRODUCT_NAME))
                price = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_PRODUCT_PRICE))
                amount = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_PRODUCT_AMOUNT))

                val emp = Cashier(id = id, transaction_id = transaction_id, date = date, time = time, product_name = product_name, price = price, amount = amount)
                empList.add(emp)

            } while (cursor.moveToNext())
        }
        return empList
    }*/

    fun updateHistory(history: History): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TRANSACTION_ID, history.transaction_id)
        values.put(KEY_TRANSACTION_DATE, history.date.toString())
        values.put(KEY_TRANSACTION_TIME, history.time.toString())
        values.put(KEY_TRANSACTION_PRODUCT_ID, history.product_id)
        values.put(KEY_TRANSACTION_PRODUCT_NAME, history.product_name)
        values.put(KEY_TRANSACTION_PRODUCT_PRICE, history.price)
        values.put(KEY_TRANSACTION_PRODUCT_AMOUNT, history.amount)

        val _success = db.update(TABLE_HISTORY, values, KEY_ID + "=?", arrayOf(history.id.toString())).toLong()
        db.close()
        return _success
    }

    @SuppressLint("Range")
    fun getHistoryDetail(_id: Int): History {
        val history = History(0,"", "", "", 0, "", 0.0, 0)
        val db = writableDatabase

        val selectQuery = "SELECT  * FROM $TABLE_HISTORY WHERE $KEY_HISTORY_ID == $_id"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    history.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_HISTORY_ID)))
                    history.transaction_id = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_ID))
                    history.date = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_DATE))
                    history.time = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_TIME))
                    history.product_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_PRODUCT_ID)))
                    history.product_name = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_PRODUCT_NAME))
                    history.price = cursor.getDouble(cursor.getColumnIndex(KEY_TRANSACTION_PRODUCT_PRICE))
                    history.amount = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_PRODUCT_AMOUNT)))

                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return history
    }

    fun deleteHistory(_id: Int): Long {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_HISTORY, KEY_HISTORY_ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return _success
    }

    //    Setting
    fun addSetting(setting: Setting): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_LOGO_PATH, setting.logo_path)
        contentValues.put(KEY_STORE_NAME, setting.store_name)
        contentValues.put(KEY_CASHIER_ACTIVE, setting.cashier_active)
        contentValues.put(KEY_LANGUAGE, setting.language)
        contentValues.put(KEY_CURRENCY, setting.currency)

        val success = db.insert(TABLE_SETTING, null, contentValues)

        db.close()
        return success
    }

    fun updateSetting(setting: Setting): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_LOGO_PATH, setting.logo_path)
        values.put(KEY_STORE_NAME, setting.store_name)
        values.put(KEY_CASHIER_ACTIVE, setting.cashier_active)
        values.put(KEY_LANGUAGE, setting.language)
        values.put(KEY_CURRENCY, setting.currency)

        val _success = db.update(TABLE_SETTING, values, KEY_ID + "=?", arrayOf(setting.id.toString())).toLong()
        db.close()
        return _success
    }

    @SuppressLint("Range")
    fun getSettingrDetail(_id: Int): Setting {
        val setting = Setting(0,"", "", 0, "", "")
        val db = writableDatabase

        val selectQuery = "SELECT  * FROM $TABLE_SETTING WHERE $KEY_SETTING_ID == $_id"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    setting.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_SETTING_ID)))
                    setting.logo_path = cursor.getString(cursor.getColumnIndex(KEY_LOGO_PATH))
                    setting.store_name = cursor.getString(cursor.getColumnIndex(KEY_STORE_NAME))
                    setting.cashier_active = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CASHIER_ACTIVE)))
                    setting.language = cursor.getString(cursor.getColumnIndex(KEY_LANGUAGE))
                    setting.currency = cursor.getString(cursor.getColumnIndex(KEY_CURRENCY))

                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return setting
    }

}