package com.harbhajan.foodie.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging
import com.harbhajan.foodie.R
import com.harbhajan.foodie.common.API
import com.harbhajan.foodie.common.printLog
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var etUserName: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var forgotPassword: TextView
    private var token: String? = ""
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin = findViewById(R.id.btnLogin)
        forgotPassword = findViewById(R.id.txtForgotPassword)
        etUserName = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        sharedPreferences =
            getSharedPreferences(getString(R.string.logged_in), Context.MODE_PRIVATE)
        val isLoogedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoogedIn) {
            val intent =
                Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Toast.makeText(this, "token not generated", Toast.LENGTH_SHORT).show()
            } else {
                // Get new FCM registration token
                token = task.result
                sharedPreferences.edit().putString("token", token).apply()
            }
        }

        btnLogin.setOnClickListener {
            val username = etUserName.text.toString()
            val password = etPassword.text.toString()

            when {
                username.length < 4 -> {
                    Toast.makeText(this@LoginActivity, "incorrect username", Toast.LENGTH_SHORT)
                        .show()
                }
                password.length < 4 -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "min length 4 characters!!!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> {
                    val queue = Volley.newRequestQueue(this@LoginActivity)

                    val jsonObjectRequest = object : JsonObjectRequest(
                        Method.GET,
                        API.getLoginUrl(username, password, token),
                        null,
                        Response.Listener {
                            try {
                                it.printLog("LOGIN")
                                val data = it.getJSONObject("data")
                                val success = data.getBoolean("success")
                                val response = data.getJSONObject("data")
                                if (success) {
                                    setSharedPreferences(response)
                                    response.printLog("LOGIN")
                                    val intent =
                                        Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()


                                }
                            } catch (e: JSONException) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "incorrect username or password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        Response.ErrorListener {
                            it.message.printLog("ERROR")
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

        forgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPassword::class.java)
            startActivity(intent)
        }
    }

    fun setSharedPreferences(response: JSONObject) {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
        sharedPreferences.edit().putString("ResId", response.getString("id")).apply()

        sharedPreferences.edit().putString("RestaurantName", response.getString("restaurant_name"))
            .apply()
        sharedPreferences.edit().putBoolean("profileSaved", false).apply()

    }
}
