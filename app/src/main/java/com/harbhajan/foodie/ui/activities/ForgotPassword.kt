package com.harbhajan.foodie.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.harbhajan.foodie.R
import com.harbhajan.foodie.common.API
import org.json.JSONException

class ForgotPassword : AppCompatActivity() {
    private lateinit var etMobileNumber: EditText
    private lateinit var etUserId: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        etMobileNumber = findViewById(R.id.etMobileNumber)
        etUserId = findViewById(R.id.etUserId)
        btnUpdate = findViewById(R.id.btnUpdate)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)


        btnUpdate.setOnClickListener {
            val mobile = etMobileNumber.text.toString()
            val userid = etUserId.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            when {
                mobile.length != 10 -> {
                    Toast.makeText(this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show()
                }
                userid.length < 4 -> {
                    Toast.makeText(this, "set userId of minimum 4 characters", Toast.LENGTH_SHORT)
                        .show()
                }
                password.length < 4 -> {
                    Toast.makeText(this, "set password of minimum 4 characters", Toast.LENGTH_SHORT)
                        .show()
                }
                password != confirmPassword -> {
                    Toast.makeText(this, "password did not confirmed", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val queue = Volley.newRequestQueue(this)
                    val jsonObjectRequest = object : JsonObjectRequest(
                        Method.GET,
                        API.getUpdateUrl(userid, mobile, password),
                        null,
                        Response.Listener {
                            try {
                                val data = it.getJSONObject("data")
                                val message = data.getString("message")
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            } catch (e: JSONException) {
                                Toast.makeText(
                                    this,
                                    "Something Went Wrong!!$e",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        },
                        Response.ErrorListener {
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

        }
    }
}