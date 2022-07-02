package com.epzigsoftwarehouse.chachingapp.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.epzigsoftwarehouse.chachingapp.products.Product

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

        db?.execSQL(CREATE_PRODUCTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }

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

    fun deleteTask(_id: Int): Long {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_PRODUCTS, KEY_ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return _success
    }
}