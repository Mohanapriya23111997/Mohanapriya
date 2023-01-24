package com.example.androidtask

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper (context:Context) : SQLiteOpenHelper(context,"records.db",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table product (id integer primary key autoincrement, productname text, quantity text, rate text, image text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVeresion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists product")
        onCreate(db)
    }

    fun insertData(name:String, qty:String, rate:String, image:String){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("productname",name)
        contentValues.put("quantity",qty)
        contentValues.put("rate",rate)
        contentValues.put("image",image)
        db.insert("product",null,contentValues)
    }

    fun updateData(id:String, name:String, email:String, password: String):Boolean{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("id",id)
        contentValues.put("name",name)
        contentValues.put("email",email)
        contentValues.put("password",password)
        db.update("details",contentValues,"id=?", arrayOf(id))
        return true
    }
}