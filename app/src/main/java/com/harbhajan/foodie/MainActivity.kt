package com.harbhajan.foodie

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.Surface
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SwitchCompat
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var txtHeaderName:TextView
    lateinit var imgHeaderPic:CircleImageView
    lateinit var txtTitle:TextView
    lateinit var btntoggle:SwitchCompat
    lateinit var sp:SharedPreferences
    var statusR:String=""
    var previousMenuItem: MenuItem?=null
   // @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout=findViewById(R.id.drawerLayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frameLayout)
        navigationView=findViewById(R.id.navigationView)
        txtTitle=findViewById(R.id.txtTitle)
        sharedPreferences = getSharedPreferences(
            getString(R.string.profile_credentials),
            Context.MODE_PRIVATE
        )!!
        sp = getSharedPreferences(
            getString(R.string.logged_in),
            Context.MODE_PRIVATE
        )!!
        val hView=navigationView.inflateHeaderView(R.layout.drawer_header)
        txtHeaderName=hView.findViewById(R.id.drawerHeaderName)
        imgHeaderPic=hView.findViewById(R.id.drawerHeaderProfile)
        btntoggle=hView.findViewById(R.id.btnToggle)

        txtHeaderName.text=sharedPreferences.getString("name","")

        val mImgUri=sharedPreferences.getString("image","")
        if (mImgUri==""){
            //todo
        }else{
            imgHeaderPic.setImageURI(Uri.parse(mImgUri))
        }
       val resStatus=sharedPreferences.getBoolean("resStatus",false)
       if(resStatus){
           setChecked(btntoggle,true)
       }else{
           setChecked(btntoggle,false)
       }
       btntoggle.setOnCheckedChangeListener { buttonView, isChecked ->
           if(isChecked){
               statusR="Open"
               statusRestaurant(statusR)

           }
           else{
               statusR="Closed"
               statusRestaurant(statusR)
           }
       }
        setUpToolbar()
        openHome()

        val actionBarDrawerToggle= ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout, R.string.drawer_open,
            R.string.drawer_close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener {
            if(previousMenuItem!==null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it
            when(it.itemId){
                R.id.gone ->{
                    openHome()
                    drawerLayout.closeDrawers()
                }
                R.id.profile->{

                    var isProfileSaved=sp.getBoolean("profileSaved",false)
                    if(isProfileSaved){
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.frameLayout,
                                SavedProfileFragment()
                            )
                            // .addToBackStack("Favourites")
                            .commit()

                        supportActionBar?.title="Profile"
                        txtTitle.text=supportActionBar?.title

                        drawerLayout.closeDrawers()
                    }else{
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.frameLayout,
                                ProfileFragment()
                            )
                            // .addToBackStack("Favourites")
                            .commit()

                        supportActionBar?.title="Profile"
                        txtTitle.text=supportActionBar?.title

                        drawerLayout.closeDrawers()
                    }

                }
                R.id.stock->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            ProductFragment()
                        )
                        // .addToBackStack("Profile")
                        .commit()

                    supportActionBar?.title="Product Stock"
                    txtTitle.text=supportActionBar?.title

                    drawerLayout.closeDrawers()
                }
                

                R.id.orders->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            OrdersFragment()
                        )
                        // .addToBackStack("Profile")
                        .commit()
                    supportActionBar?.title="Orders"
                    txtTitle.text=supportActionBar?.title

                    drawerLayout.closeDrawers()
                }

                R.id.money ->{
                    val dialog= MoneyFragment()
                    dialog.show(supportFragmentManager,"custom dialog")
                }
                R.id.reports ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            ReportFragment()
                        )
                        //.addToBackStack("AboutApp")
                        .commit()
                    supportActionBar?.title="Report"
                    txtTitle.text=supportActionBar?.title

                    drawerLayout.closeDrawers()
                }
                R.id.support ->{
                    /*supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            SupportFragment()
                        )
                        //.addToBackStack("AboutApp")
                        .commit()
                    supportActionBar?.title="Support Centre"
                    drawerLayout.closeDrawers()

                     */
                    val dialog= ContactUsDialogBox()
                    dialog.show(supportFragmentManager,"custom dialog")
                }
               /* R.id.status ->{
                    /*supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            SupportFragment()
                        )
                        //.addToBackStack("AboutApp")
                        .commit()
                    supportActionBar?.title="Support Centre"
                    drawerLayout.closeDrawers()

                     */
                    val dialog= StatusDialogBox()
                    dialog.show(supportFragmentManager,"custom dialog")
                }

                */
             /*   R.id.shareApp->{
                    /*supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            ShareAppFragment()
                        )
                        //.addToBackStack("AboutApp")
                        .commit()
                    supportActionBar?.title="Share App"
                    drawerLayout.closeDrawers()

                     */
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Hey Check out this Great app:"
                    )
                    sendIntent.type = "text/plain"
                    startActivity(sendIntent)
                }

              */
                R.id.logout->{

                    /*supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            ShareAppFragment()
                        )
                        //.addToBackStack("AboutApp")
                        .commit()
                    supportActionBar?.title="Share App"
                    drawerLayout.closeDrawers()

                     */

                    sp.edit().clear().apply()
                    sharedPreferences.edit().clear().apply()
                    val intent=Intent(this@MainActivity,LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)

        supportActionBar?.title="Latest Assigned Orders"
        txtTitle.text=supportActionBar?.title
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if (id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openHome(){
        val fragment=NotificationFragment()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout,fragment)
        transaction.commit()

        supportActionBar?.title="Latest Assigned Orders"
        txtTitle.text=supportActionBar?.title
        navigationView.setCheckedItem(R.id.gone)
    }
    //to open dashboard after pressing back button
    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frameLayout)
        when(frag) {
            !is NotificationFragment -> openHome()
            else -> super.onBackPressed()
        }

    }
    private fun statusRestaurant(status:String){
        val resId=sp.getString("ResId","")
        val queue = Volley.newRequestQueue(this)
        val url ="http://techblr.xyz/admin/restaurant_status/"
        val jsonParams= JSONObject()

        var uid="?rid=$resId"
        var ustatus="&status=$status"
        jsonParams.put("rid",resId)
        jsonParams.put("status",status)
        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url + uid + ustatus   , jsonParams, Response.Listener {
            try {
                val data = it.getJSONObject("data")
                val message = data.getString("message")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                if(status=="Open") {
                    sharedPreferences.edit().putBoolean("resStatus", true).apply()
                }else{
                    sharedPreferences.edit().putBoolean("resStatus", false).apply()
                }

            }catch (e: JSONException){
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
    fun setChecked(switch: SwitchCompat, checked: Boolean) {
        (switch as Checkable).isChecked = checked
    }

}