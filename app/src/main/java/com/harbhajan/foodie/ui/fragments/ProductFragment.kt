package com.harbhajan.foodie.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.harbhajan.foodie.R
import com.harbhajan.foodie.adapters.ProductRecyclerAdapter
import com.harbhajan.foodie.common.printLog
import com.harbhajan.foodie.modals.Products
import org.json.JSONException

class ProductFragment : Fragment() {
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: ProductRecyclerAdapter
    lateinit var recyclerProduct: RecyclerView
    lateinit var progressLayout: RelativeLayout
    private var productDetails = ArrayList<Products>()
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)
        progressLayout = view.findViewById(R.id.progressLayout)
        recyclerProduct = view.findViewById(R.id.recyclerProduct)
        sharedPreferences =
            context?.getSharedPreferences(getString(R.string.logged_in), Context.MODE_PRIVATE)!!
        val resId = sharedPreferences.getString("ResId", "")
        // Inflate the layout for this fragment
        /*  var productList= arrayListOf<Products>(
              Products("OnePlus","SmartPhone","500","http/31")
          )

         */

        layoutManager = LinearLayoutManager(activity)

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "https://admin.manadelivery.in/admin/rproducts/"
        val urlId = "?rid=$resId"
        val jsonObjectRequest =
            object : JsonObjectRequest(Method.GET, url + urlId, null, Response.Listener {
                try {
                    progressLayout.visibility = View.GONE
                    val data = it.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val menuItem = data.getJSONObject(i)
                        val productItems = Products(
                            menuItem.getString("id"),
                            menuItem.getString("product"),
                            menuItem.getString("image"),
                            menuItem.getString("stock"),
                            menuItem.getString("total_price")
                        )

                        productDetails.add(productItems)

                        recyclerAdapter =
                            ProductRecyclerAdapter(activity as Context, productDetails)
                        recyclerProduct.adapter = this.recyclerAdapter
                        recyclerProduct.layoutManager = this.layoutManager
                    }


                } catch (e: JSONException) {
                    Toast.makeText(
                        activity as Context,
                        "Some Unexpected Error Occurred $e",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }, Response.ErrorListener {
                it.printLog("PRODUCT_PAGE")
                println("Error is $it")
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
        queue.add(jsonObjectRequest)
        return view
    }
}