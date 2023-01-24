package com.example.androidtask

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import java.io.InputStream
import java.net.URL


class productlist : AppCompatActivity() {
    var pojolists: ArrayList<pojolist>? = null
    var adapter: MyRecyclerViewAdapter? = null
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productlist)
        pojolists = ArrayList()
        recyclerView = findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(this@productlist)
        val retrofit = Retrofit.Builder()
            .baseUrl(URLConstant.url)
            .build()
        //Create Service
        val service = retrofit.create(APIService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val url = "5def7b172f000063008e0aa2"
            val response = service.product(url)
            Log.e("resdfg",""+response)
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()?.string()
                        )
                    )
                    val res = JSONObject(prettyJson)
                    val resbody = res.getJSONArray("products")
                //    val finalres = resbody.getJSONObject(0)
                    pojolists!!.clear()
                    Log.e("finalres",""+resbody)
                    for (i in 0 until resbody.length()){
                        val map = resbody.getJSONObject(i)
                        val pojolist = pojolist()
                      //  pojolist.id =  map.getString("id")
                        pojolist.name =  map.getString("name")
                        pojolist.image =  map.getString("image")
                        pojolist.qty = map.getString("quantity")
                        pojolist.rate = map.getString("price")

                        pojolists!!.add(pojolist)
                    }
                    adapter = MyRecyclerViewAdapter(
                        this@productlist,
                        pojolists!!
                    )
                    recyclerView.adapter = adapter
                }
            }
        }

    }
    class MyRecyclerViewAdapter internal constructor(
        context: Context?,
        data: MutableList<pojolist>,

        ) :
    RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

        private val mData: MutableList<pojolist>
        private val mInflater: LayoutInflater
        private val arraylist: MutableList<pojolist>
        val context = context;
        lateinit var sharedpreferences: SharedPreferences
        val MyPREFERENCES = "MyPrefs"

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val view: View
            view = mInflater.inflate(R.layout.list, parent, false)
            sharedpreferences = view.context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val pojo = mData[position]
            holder.productname.setText(pojo.name)
            holder.rate.setText(pojo.rate)
            holder.qty.setText(pojo.qty)

            Picasso.with(context)
                .load(pojo.image)
                .into(holder.images);
            lateinit var dbHelper: DatabaseHelper
            dbHelper = DatabaseHelper(context!!)
            val db = dbHelper.writableDatabase

            val query1 = "select * from product where productname='" + pojo.name + "'"
            Log.e("queries", query1)
            val cr1: Cursor = db.rawQuery(query1, null)
            if (cr1.count > 0) {
                if (cr1.moveToFirst()) {
                    do {
                       holder.view.visibility = View.VISIBLE
                        holder.cart.visibility = View.GONE
                    } while (cr1.moveToNext())
                }
            }


            holder.cart.setOnClickListener {

                try {
                    dbHelper.insertData(pojo.name.toString(), pojo.qty.toString(), pojo.rate.toString(), pojo.image.toString())
                    Toast.makeText(context,"Added Successfully", Toast.LENGTH_LONG).show()
                    holder.view.visibility = View.VISIBLE
                    holder.cart.visibility = View.GONE
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }

            holder.view.setOnClickListener {
                val editor = sharedpreferences.edit()
                editor.putString("name",pojo.name.toString())
                editor.commit()
                val intent = Intent(holder.itemView.getContext(), view::class.java)
                holder.itemView.getContext().startActivity(intent)
            }

        }

        override fun getItemCount(): Int {
            return mData.size
        }
        // stores and recycles views as they are scrolled off screen
        inner class ViewHolder internal constructor(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            var images: ImageView
            var productname: TextView
            var rate: TextView
            var qty: TextView
            var cart: ImageView
            var view: ImageView

            init {
                images = itemView.findViewById(R.id.images)
                productname = itemView.findViewById(R.id.productname)
                rate = itemView.findViewById(R.id.rate)
                qty = itemView.findViewById(R.id.qty)
                cart = itemView.findViewById(R.id.addcart)
                view = itemView.findViewById(R.id.view)


            }
        }
        init {
            mInflater = LayoutInflater.from(context)
            mData = data
            arraylist = java.util.ArrayList()
            arraylist.addAll(data)
        }
    }

}

