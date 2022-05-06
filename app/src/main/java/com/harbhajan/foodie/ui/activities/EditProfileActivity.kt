package com.harbhajan.foodie.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.harbhajan.foodie.R
import de.hdodenhof.circleimageview.CircleImageView

class EditProfileActivity : AppCompatActivity() {
    companion object {
        private const val Image_Pick_Code = 1000;
        private const val Permission_Code = 1001;
    }

    private lateinit var imgBurgerProfile: ImageView
    private lateinit var imgProfilePic: CircleImageView
    private lateinit var etNameProfile: EditText

    // lateinit var etHandleProfile: EditText
    private lateinit var etMobileProfile: EditText
    private lateinit var etDescriptionProfile: EditText
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sp: SharedPreferences
    private lateinit var saveProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        imgBurgerProfile = findViewById(R.id.imgBurgerProfile)
        imgProfilePic = findViewById(R.id.imgProfilePic)
        etNameProfile = findViewById(R.id.txtNameProfile)
        // etHandleProfile =findViewById(R.id.txtHandleProfile)
        etMobileProfile = findViewById(R.id.txtMobileProfile)
        etDescriptionProfile = findViewById(R.id.txtDescriptionProfile)
        saveProfile = findViewById(R.id.btnSaveProfile)
        sharedPreferences =
            getSharedPreferences(
                getString(R.string.profile_credentials),
                Context.MODE_PRIVATE
            )
        sp =
            getSharedPreferences(
                getString(R.string.logged_in),
                Context.MODE_PRIVATE
            )
        imgProfilePic.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //run time popup for request
                    requestPermissions(permission, Permission_Code)
                } else {
                    //permission already granted
                    pickImageFromGallery()
                }
            } else {
                // android version<=Marshmello
                pickImageFromGallery()
            }
        }

        saveProfile.setOnClickListener {
            val name = etNameProfile.text.toString()
            // val handle=etHandleProfile.text.toString()
            val mobile = etMobileProfile.text.toString()
            val description = etDescriptionProfile.text.toString()
            if (name != "" && mobile != "" && description != "") {

                if (mobile.length == 10) {
                    setSharedPreferences()
                    Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show()
                    // sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
                    sp.edit().putBoolean("profileSaved", true).apply()
                    val intent = Intent(this, SavedProfileActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "wrong number", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "SomeThing Went Wrong!!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun pickImageFromGallery() {
        //intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, Image_Pick_Code)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Permission_Code -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission denied
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Image_Pick_Code) {
            try {

                val mImgUri = data?.data

                // Saves image URI as string to Default Shared Preferences
                sharedPreferences.edit().putString("image", mImgUri.toString()).apply()

                // Sets the ImageView with the Image URI
                imgProfilePic.setImageURI(mImgUri)
                // imgProfile.invalidate()

            } catch (e: Exception) {
                Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setSharedPreferences() {
        sharedPreferences.edit()
            .putString("name", etNameProfile.text.toString()).apply()
        sharedPreferences.edit()
            .putString("mobile", etMobileProfile.text.toString()).apply()
        // sharedPreferences.edit().putString("handle", etHandleProfile.text.toString()).apply()
        sharedPreferences.edit().putString("description", etDescriptionProfile.text.toString())
            .apply()


    }
}