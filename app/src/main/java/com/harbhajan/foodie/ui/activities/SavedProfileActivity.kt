package com.harbhajan.foodie.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.harbhajan.foodie.R
import de.hdodenhof.circleimageview.CircleImageView

class SavedProfileActivity : AppCompatActivity() {
    lateinit var imgBurgerProfile: ImageView
    lateinit var imgProfilePic: CircleImageView
    lateinit var etNameProfile: TextView
    //lateinit var etHandleProfile: TextView
    lateinit var etMobileProfile: TextView
    lateinit var etDescriptionProfile: TextView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var btnEdit:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_profile)
        imgBurgerProfile =findViewById(R.id.imgBurgerProfile)
        imgProfilePic =findViewById(R.id.imgProfilePic)
        etNameProfile = findViewById(R.id.txtNameProfile)
      //  etHandleProfile =findViewById(R.id.txtHandleProfile)
        etMobileProfile = findViewById(R.id.txtMobileProfile)
        etDescriptionProfile =findViewById(R.id.txtDescriptionProfile)
        btnEdit=findViewById(R.id.btnEditProfile)
        sharedPreferences = getSharedPreferences(
                getString(R.string.profile_credentials),
                Context.MODE_PRIVATE
            )!!
        etNameProfile.text=sharedPreferences.getString("name","")
      //  etHandleProfile.text=sharedPreferences.getString("handle","")
        etMobileProfile.text=sharedPreferences.getString("mobile","")
        etDescriptionProfile.text=sharedPreferences.getString("description","")
        val mImgUri=sharedPreferences.getString("image","")
        imgProfilePic.setImageURI(Uri.parse(mImgUri))
        btnEdit.setOnClickListener {
            val intent=Intent(this@SavedProfileActivity, EditProfileActivity()::class.java)
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        val intent= Intent(this@SavedProfileActivity, MainActivity::class.java)
        startActivity(intent)
        }
}