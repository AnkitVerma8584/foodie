package com.harbhajan.foodie.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputLayout
import com.harbhajan.foodie.R
import com.harbhajan.foodie.modals.Notification
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

class RecyclerNotificationAdapter(
    private val context: Context,
    private val notificationList: ArrayList<Notification>
) : RecyclerView.Adapter<RecyclerNotificationAdapter.HomeViewHolder>() {

    private val limit = 5
    var wTime: String? = ""
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_recycler_notification_adapter, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (notificationList.size > limit) {
            limit
        } else {
            notificationList.size
        }

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val commonUrl = "https://admin.manadelivery.in/admin/img/"
        sharedPreferences = context.getSharedPreferences("Log In", Context.MODE_PRIVATE)
        val times = context.resources.getStringArray(R.array.Time)
        val order = notificationList[position]
        val items: String = order.quantity
        holder.id.text = order.id
        holder.productName.text = order.product
        holder.restaurantName.text = order.restaurant_name
        holder.time.text = order.time
        holder.quantity.text = "Quantity:$items"
        holder.orderId.text = order.order_id
        holder.address.text = order.address
        holder.bill.text = order.total_amount
        holder.customerName.text = order.name
        holder.customerMobile.text = order.mobile


        //holder.time.text=order.time
        Picasso.get().load(commonUrl + order.image).error(R.drawable.black_panther)
            .into(holder.productImage)


        val arrayAdapter = ArrayAdapter(context, R.layout.spinner_item, times)
        // get reference to the autocomplete text view

        holder.txtSpinner.setAdapter(arrayAdapter)

        holder.send.setOnClickListener {
            wTime = holder.txtSpinner.text.toString()
            if (wTime.isNullOrEmpty()) {
                Toast.makeText(context, "Please select waiting time!!!", Toast.LENGTH_SHORT).show()
            } else {
                val id = order.id
                val rid = sharedPreferences.getString("ResId", "")
                val queue = Volley.newRequestQueue(context)
                val url = "https://admin.manadelivery.in/admin/restaurant_time/"
                val jsonParams = JSONObject()
                val urid = "?rid=$rid"
                val uid = "&id=$id"
                val utime = "&r_time=$wTime"

                jsonParams.put("rid", rid)
                jsonParams.put("id", id)
                jsonParams.put("r_time", wTime)
                val jsonObjectRequest = object : JsonObjectRequest(
                    Method.POST,
                    url + urid + uid + utime,
                    jsonParams,
                    Response.Listener {
                        try {
                            val data = it.getJSONObject("data")
                            val message = data.getString("Message")
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            // holder.txtWaitingTime.text= "Waiting time for delivery boy is: $wTime"
                            // holder.spinner.visibility = View.GONE
                            holder.txtWaitingTime.visibility = View.INVISIBLE
                            //holder.txtWaitingTime.text="Waiting time for delivery boy is: $wTime"

                            holder.txtSpinner.visibility = View.INVISIBLE
                            holder.dropMenu.visibility = View.INVISIBLE
                            holder.send.visibility = View.INVISIBLE
                            Toast.makeText(
                                context,
                                "Waiting time for delivery boy is: $wTime",
                                Toast.LENGTH_SHORT
                            ).show()


                        } catch (e: JSONException) {
                            Toast.makeText(
                                context,
                                "Something Went Wrong!!$e",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    },
                    Response.ErrorListener {
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
        holder.btnOrderReady.setOnClickListener {
            val id = order.id
            //val rid = sharedPreferences.getString("ResId", "")
            val queue = Volley.newRequestQueue(context)
            val url = "https://admin.manadelivery.in/admin/update_rorders_status/"
            val jsonParams = JSONObject()

            // val urid = "?rid=$rid"
            val uid = "?id=$id"
            val roStatus = "&ro_status=Completed"

            //jsonParams.put("rid", rid)
            jsonParams.put("id", id)
            jsonParams.put("ro_status", "Completed")
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.POST,
                url + uid + roStatus,
                jsonParams,
                Response.Listener {
                    try {
                        val data = it.getJSONObject("data")
                        val message = data.getString("message")
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        holder.btnOrderReady.visibility = View.GONE
                        holder.txtOrderReady.visibility = View.GONE


                    } catch (e: JSONException) {
                        Toast.makeText(
                            context,
                            "Something Went Wrong!!$e",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                },
                Response.ErrorListener {
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


    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val id: TextView = view.findViewById(R.id.txtId)
        val orderId: TextView = view.findViewById(R.id.txtOrderId)
        val productName: TextView = view.findViewById(R.id.txtProductName)
        val restaurantName: TextView = view.findViewById(R.id.txtRestaurantName)
        val address: TextView = view.findViewById(R.id.txtCustomerAddress)
        val bill: TextView = view.findViewById(R.id.txtBill)
        val time: TextView = view.findViewById(R.id.txtTime)
        val productImage: ImageView = view.findViewById(R.id.imgProductImage)
        val quantity: TextView = view.findViewById(R.id.txtQuantity)
        val customerName: TextView = view.findViewById(R.id.txtCustomerName)
        val customerMobile: TextView = view.findViewById(R.id.txtCustomerPhone)
        val txtWaitingTime: TextView = view.findViewById(R.id.txtWaitingTime)
        val send: Button = view.findViewById(R.id.btnSend)
        val btnOrderReady: Button = view.findViewById(R.id.btnOrderReady)
        val txtSpinner: AutoCompleteTextView = view.findViewById(R.id.txtSpinner)
        val txtOrderReady: TextView = view.findViewById(R.id.txtOrderReady)
        val dropMenu: TextInputLayout = view.findViewById(R.id.dropDownMenu)
    }
}