package com.example.androidtask

import android.content.SharedPreferences
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class view : AppCompatActivity() {
    lateinit var name: TextView
    lateinit var qty: TextView
    lateinit var rate: TextView
    lateinit var image: ImageView
    lateinit var add: ImageView
    lateinit var minus: ImageView
    lateinit var sharedpreferences: SharedPreferences
    val MyPREFERENCES = "MyPrefs"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        name = findViewById(R.id.name)
        image = findViewById(R.id.image)
        qty = findViewById(R.id.qty)
        rate = findViewById(R.id.rate)
        add = findViewById(R.id.add)
        minus = findViewById(R.id.minus)
        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE)
        lateinit var dbHelper: DatabaseHelper
        dbHelper = DatabaseHelper(applicationContext)
        val db = dbHelper.writableDatabase

        val query1 = "select * from product where productname='" +sharedpreferences.getString("name","")+ "'"
        Log.e("queries", query1)
        val cr1: Cursor = db.rawQuery(query1, null)
        if (cr1.count > 0) {
            if (cr1.moveToFirst()) {
                do {
                    name.setText(cr1.getString(cr1.getColumnIndexOrThrow("productname")))
                    qty.setText(cr1.getString(cr1.getColumnIndexOrThrow("quantity")))
                    rate.setText(cr1.getString(cr1.getColumnIndexOrThrow("rate")))
                    Picasso.with(applicationContext)
                        .load(cr1.getString(cr1.getColumnIndexOrThrow("image")))
                        .into(image);

                } while (cr1.moveToNext())
            }
        }

        add.setOnClickListener(View.OnClickListener {

                var x: Int = qty.getText().toString().toInt()
                if (x == 0) {
                    qty.setText("1")
                } else {
                    // x++;
                    x += 1
                   qty.setText(x.toString())
                }

            db.execSQL("UPDATE product SET quantity='"+qty+"' where productname='"+name+"'")
        })
        minus.setOnClickListener(View.OnClickListener {

                var x: Int = qty.getText().toString().toInt()
                if (x == 0) {
                } else {
                    //  x--;
                    x -= 1
                    if (x < 0) {
                        x = 0
                    }
                    qty.setText(x.toString())
                }

            db.execSQL("UPDATE product SET quantity='"+qty+"' where productname='"+name+"'")
        })

    }
}