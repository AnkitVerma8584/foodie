package com.harbhajan.foodie.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.harbhajan.foodie.R
import com.harbhajan.foodie.adapters.OrderRecyclerAdapter
import com.harbhajan.foodie.common.API
import com.harbhajan.foodie.modals.Orders
import org.json.JSONException


class OrdersFragment : Fragment() {
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerAdapter: OrderRecyclerAdapter
    private lateinit var recyclerOrder: RecyclerView
    private lateinit var progressLayout: RelativeLayout
    private lateinit var progressBar: ProgressBar
    private var orderDetails = ArrayList<Orders>()
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_orders, container, false)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        recyclerOrder = view.findViewById(R.id.recyclerOrder)
        sharedPreferences =
            context?.getSharedPreferences(getString(R.string.logged_in), Context.MODE_PRIVATE)!!

        val resId: String = sharedPreferences.getString("ResId", "")!!
        layoutManager = LinearLayoutManager(activity)
        val queue = Volley.newRequestQueue(activity as Context)

        val jsonObjectRequest =
            object : JsonObjectRequest(Method.GET, API.getOrders(resId), null, Response.Listener {
                try {
                    progressLayout.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    val data = it.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val menuItem = data.getJSONObject(i)
                        val orderItems = Orders(
                            menuItem.getString("id"),
                            menuItem.getString("name"),
                            menuItem.getString("mobile"),
                            menuItem.getString("order_id"),
                            menuItem.getString("address"),
                            menuItem.getString("total_item"),
                            menuItem.getString("total_amount")
                        )

                        orderDetails.add(orderItems)

                        recyclerAdapter = OrderRecyclerAdapter(activity as Context, orderDetails)
                        recyclerOrder.adapter = this.recyclerAdapter
                        recyclerOrder.layoutManager = this.layoutManager
                    }


                } catch (e: JSONException) {
                    Toast.makeText(
                        activity as Context,
                        "No Orders!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }, Response.ErrorListener {
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