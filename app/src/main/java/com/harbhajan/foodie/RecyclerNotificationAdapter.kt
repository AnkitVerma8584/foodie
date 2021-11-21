package com.harbhajan.foodie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.DropBoxManager
import android.os.Trace.isEnabled
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import android.widget.ArrayAdapter
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_order_history.view.*
import org.json.JSONException
import org.json.JSONObject

class RecyclerNotificationAdapter(val context: Context, val notificationList: ArrayList<Notification>): RecyclerView.Adapter<RecyclerNotificationAdapter.HomeViewHolder>() {

    private val limit=5
    var wTime:String?=""
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.activity_recycler_notification_adapter,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(notificationList.size>limit){
            limit
        }else{
            notificationList.size
        }

    }


    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val commonUrl = "http://www.techblr.xyz/admin/img/"
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

        /*holder.txtSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                 wTime=parent.getItemAtPosition(position).toString()
               /* if (parent.getItemAtPosition(position).equals("Select Time")){
                    // holder.spinner.setSelection(position)
                    // holder.spinner.isEnabled=false
                }else{
                    wTime=parent.getItemAtPosition(position).toString()
                    //Toast.makeText(parent.context,"selected +$wTime",Toast.LENGTH_SHORT).show()
                    //holder.txtSpinner.setSelection(position)
                }

                */
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(context,"Please select any time",Toast.LENGTH_SHORT).show()
            }
        }

         */



        /*holder.etTimeD.addTextChangedListener {
            holder.send.isEnabled =!(it.isNullOrEmpty())
        }

         */
       /* if (holder.spinner != null) {
            val adapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item, times
            )
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            holder.spinner.adapter = adapter

            holder.spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    if (parent.getItemAtPosition(position).equals("Select Time")){
                       // holder.spinner.setSelection(position)
                       // holder.spinner.isEnabled=false
                    }else{
                        wTime=parent.getItemAtPosition(position).toString()
                        //Toast.makeText(parent.context,"selected +$wTime",Toast.LENGTH_SHORT).show()
                        holder.spinner.setSelection(position)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(context,"Please select any time",Toast.LENGTH_SHORT).show()
                }
            }

        */
            holder.send.setOnClickListener {
                wTime=holder.txtSpinner.text.toString()
                if(wTime.isNullOrEmpty() || wTime=="Select Time") {
                    Toast.makeText(context, "Please select waiting time!!!", Toast.LENGTH_SHORT).show()
                }else{
                    val id = order.id
                    val rid = sharedPreferences.getString("ResId", "")
                    val queue = Volley.newRequestQueue(context)
                    val url = "http://techblr.xyz/admin/restaurant_time/"
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
                                holder.txtWaitingTime.visibility=View.INVISIBLE
                                //holder.txtWaitingTime.text="Waiting time for delivery boy is: $wTime"

                                holder.txtSpinner.visibility=View.INVISIBLE
                                holder.dropMenu.visibility=View.INVISIBLE
                                holder.send.visibility = View.INVISIBLE
                                Toast.makeText(context, "Waiting time for delivery boy is: $wTime", Toast.LENGTH_SHORT).show()


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
                val url = "http://techblr.xyz/admin/update_rorders_status/"
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
            val txtWaitingTime:TextView=view.findViewById(R.id.txtWaitingTime)
            val send: Button = view.findViewById(R.id.btnSend)
            val btnOrderReady: Button = view.findViewById(R.id.btnOrderReady)
            val txtSpinner:AutoCompleteTextView=view.findViewById(R.id.txtSpinner)
            val txtOrderReady: TextView = view.findViewById(R.id.txtOrderReady)
            val dropMenu:TextInputLayout=view.findViewById(R.id.dropDownMenu)


        }
}