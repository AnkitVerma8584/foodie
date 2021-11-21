package com.harbhajan.foodie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class ForgotPassword : AppCompatActivity() {
    lateinit var etMobileNumber:EditText
    lateinit var etUserId:EditText
    lateinit var etPassword:EditText
    lateinit var etConfirmPassword:EditText
    lateinit var btnUpdate:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        etMobileNumber=findViewById(R.id.etMobileNumber)
        etUserId=findViewById(R.id.etUserId)
        btnUpdate=findViewById(R.id.btnUpdate)
        etPassword=findViewById(R.id.etPassword)
        etConfirmPassword=findViewById(R.id.etConfirmPassword)


        btnUpdate.setOnClickListener {
            var mobile=etMobileNumber.text.toString()
            var userid=etUserId.text.toString()
            var password=etPassword.text.toString()
            var confirmPassword=etConfirmPassword.text.toString()
            if(mobile.length!=10){
                Toast.makeText(this,"Invalid Mobile Number",Toast.LENGTH_SHORT).show()
            }else if(userid.length<4){
                Toast.makeText(this,"set userId of minimum 4 characters",Toast.LENGTH_SHORT).show()
            }
            else if (password.length<4){
                Toast.makeText(this,"set password of minimum 4 characters",Toast.LENGTH_SHORT).show()
            }else if(password!=confirmPassword){
                Toast.makeText(this,"password did not confirmed",Toast.LENGTH_SHORT).show()
            }else{
                val queue = Volley.newRequestQueue(this)
                val url ="http://techblr.xyz/admin/restaurant_updatePass/"
                val jsonParams=JSONObject()

                var uid="?userid=$userid"
                var uphone="&phone=$mobile"
                var upass="&password=$password"

                val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url + uid + upass +uphone   , null, Response.Listener {
                    try {
                        val data = it.getJSONObject("data")
                        val message = data.getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }catch (e:JSONException){
                        Toast.makeText(
                            this,
                            "Something Went Wrong!!$e",
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
                        headers["Content-Type"]="application/json"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)
            }

        }
    }
}