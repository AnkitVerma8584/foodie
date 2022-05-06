package com.harbhajan.foodie.ui.activities

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.harbhajan.foodie.R
import com.harbhajan.foodie.adapters.AllProductsAdapter
import com.harbhajan.foodie.modals.AllProducts
import org.json.JSONException

class OrderHistoryActivity : AppCompatActivity() {
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerAdapter: AllProductsAdapter
    private lateinit var recyclerOrder: RecyclerView
    private var orderDetails = ArrayList<AllProducts>()
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private var orderId: String? = ""
    private var oid: String? = ""

    private var address: String? = ""
    private var bill: String? = ""
    private var totalItems: String? = ""
    private var name: String? = ""
    private var mobile: String? = ""

    private lateinit var txtOrderId: TextView
    private lateinit var txtTime: TextView
    private lateinit var txtItemNO: TextView
    private lateinit var txtAddress: TextView
    private lateinit var txtMoney: TextView

    private lateinit var txtCustomerName: TextView

    //lateinit var txtCustomerMobile:TextView
    private lateinit var txtSubTotal: TextView
    private lateinit var txtTax: TextView
    private lateinit var txtTotal: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        if (intent != null) {
            orderId = intent.getStringExtra("OrderId")
            address = intent.getStringExtra("Address")
            bill = intent.getStringExtra("Bill")
            totalItems = intent.getStringExtra("items")
            name = intent.getStringExtra("Name")
            mobile = intent.getStringExtra("Mobile")
            oid = intent.getStringExtra("Oid")
        } else {
            Toast.makeText(this@OrderHistoryActivity, "Something went wrong!1", Toast.LENGTH_SHORT)
                .show()
            onBackPressed()
        }
        recyclerOrder = findViewById(R.id.recyclerOrder)
        toolbar = findViewById(R.id.toolbar)
        txtOrderId = findViewById(R.id.txtOrderId)
        txtTime = findViewById(R.id.txtTime)
        txtItemNO = findViewById(R.id.txtItemsNo)
        txtAddress = findViewById(R.id.txtCustomerAddress)

        txtMoney = findViewById(R.id.txtMoney)

        txtCustomerName = findViewById(R.id.txtCustomerName)
        //txtCustomerMobile=findViewById(R.id.txtCustomerPhone)
        txtSubTotal = findViewById(R.id.txtSubTotalRupee)
        txtTax = findViewById(R.id.txtTaxRupee)
        txtTotal = findViewById(R.id.txtTotalRupee)
        toolbar.title = "Order Details"
        layoutManager = LinearLayoutManager(this)
        oid?.let { allProducts(it) }


        val t = bill?.toInt()
        val tax = (t?.times(0.1))
        val sub = t?.minus(tax!!)
        txtSubTotal.text = "$sub"
        txtTax.text = "$tax"
        txtTotal.text = bill

        txtOrderId.text = orderId
        txtItemNO.text = "Item:$totalItems"
        txtMoney.text = bill
        txtAddress.text = address

        txtCustomerName.text = name
        //txtCustomerMobile.text=mobile
        // val commonUrl="http://www.techblr.xyz/admin/img/"

    }

    private fun allProducts(oid: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://admin.manadelivery.in/admin/restaurant_orderedProducts/"
        val uid = "?id=$oid"
        val jsonObjectRequest =
            object : JsonObjectRequest(Method.GET, url + uid, null, Response.Listener {
                try {

                    val data = it.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val menuItem = data.getJSONObject(i)
                        val orderItems = AllProducts(
                            menuItem.getString("product_id"),
                            menuItem.getString("product"),
                            menuItem.getString("image"),
                            menuItem.getString("product_price"),
                            menuItem.getString("product_quantity"),
                            menuItem.getString("time")
                        )

                        orderDetails.add(orderItems)

                        recyclerAdapter = AllProductsAdapter(this, orderDetails)
                        recyclerOrder.adapter = this.recyclerAdapter
                        recyclerOrder.layoutManager = this.layoutManager
                    }


                } catch (e: JSONException) {
                    Toast.makeText(
                        this@OrderHistoryActivity,
                        "Some Unexpected Error Occurred $e",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }, Response.ErrorListener {
                /*if (activity != null) {
                    Toast.makeText(
                        activity as Context,
                        "Volley Error Occurred!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                 */
                println("Error is $it")
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
        queue.add(jsonObjectRequest)
    }
}