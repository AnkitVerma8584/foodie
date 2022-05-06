package com.harbhajan.foodie.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harbhajan.foodie.modals.AllProducts
import com.harbhajan.foodie.R
import com.harbhajan.foodie.common.API
import com.squareup.picasso.Picasso

class AllProductsAdapter(val context: Context, val productList: ArrayList<AllProducts>) :
    RecyclerView.Adapter<AllProductsAdapter.HomeViewHolder>() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.all_products_adapter, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        sharedPreferences = context.getSharedPreferences("Log In", Context.MODE_PRIVATE)!!
        val resName = sharedPreferences.getString("RestaurantName", "")
        val order = productList[position]
        val items: String = order.quantity
        holder.product.text = order.product
        holder.quantity.text = "Quantity:$items"
        holder.price.text = order.price
        holder.productId.text = order.productId
        holder.time.text = order.time
        holder.restaurant.text = resName
        Picasso.get().load(API.IMAGE_URL + order.image).error(R.drawable.black_panther)
            .into(holder.image)

    }


    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imgDish)
        val product: TextView = view.findViewById(R.id.txtProductName)
        val restaurant: TextView = view.findViewById(R.id.txtRestaurantName)
        val quantity: TextView = view.findViewById(R.id.txtQuantity)
        val time: TextView = view.findViewById(R.id.txtTime)
        val price: TextView = view.findViewById(R.id.txtPrice)
        val productId: TextView = view.findViewById(R.id.txtProductId)
    }
}