package com.harbhajan.foodie

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

class EditProfileActivity : AppCompatActivity() {
    companion object
    {
        private val Image_Pick_Code = 1000;
        private val Permission_Code = 1001;
    }

    lateinit var imgBurgerProfile: ImageView
    lateinit var imgProfilePic: CircleImageView
    lateinit var etNameProfile: EditText
   // lateinit var etHandleProfile: EditText
    lateinit var etMobileProfile: EditText
    lateinit var etDescriptionProfile: EditText
    lateinit var sharedPreferences: SharedPreferences
    lateinit var sp: SharedPreferences
    lateinit var saveProfile: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        imgBurgerProfile =findViewById(R.id.imgBurgerProfile)
        imgProfilePic =findViewById(R.id.imgProfilePic)
        etNameProfile =findViewById(R.id.txtNameProfile)
       // etHandleProfile =findViewById(R.id.txtHandleProfile)
        etMobileProfile =findViewById(R.id.txtMobileProfile)
        etDescriptionProfile =findViewById(R.id.txtDescriptionProfile)
        saveProfile=findViewById(R.id.btnSaveProfile)
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
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if(ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permission= arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    //run time popup for request
                    requestPermissions(permission, EditProfileActivity.Permission_Code)
                }else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }else{
                // android version<=Marshmello
                pickImageFromGallery()
            }
        }

        saveProfile.setOnClickListener {
            val name=etNameProfile.text.toString()
           // val handle=etHandleProfile.text.toString()
            val mobile=etMobileProfile.text.toString()
            val description=etDescriptionProfile.text.toString()
            if (name!=""  && mobile!=""&& description!="") {

                if (mobile.length == 10) {
                    setSharedPreferences()
                    Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show()
                    // sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
                    sp.edit().putBoolean("profileSaved",true).apply()
                    val intent= Intent(this,SavedProfileActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "wrong number", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "SomeThing Went Wrong!!", Toast.LENGTH_SHORT).show()
            }
        }

    }

private fun pickImageFromGallery() {
    //intent to pick image
    val intent=Intent(Intent.ACTION_PICK)
    intent.type="image/*"
    startActivityForResult(intent, EditProfileActivity.Image_Pick_Code)
}

override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    when(requestCode){
        EditProfileActivity.Permission_Code ->{
            if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //permission from popup granted
                pickImageFromGallery()
            }else{
                //permission denied
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show()
            }
        }
    }
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode== Activity.RESULT_OK && requestCode== EditProfileActivity.Image_Pick_Code){
        try {

            val mImgUri=data?.data

            // Saves image URI as string to Default Shared Preferences
            sharedPreferences.edit().putString("image", mImgUri.toString()).apply()

            // Sets the ImageView with the Image URI
            imgProfilePic.setImageURI(mImgUri)
            // imgProfile.invalidate()

        }catch (e: Exception){
            Toast.makeText(this,"$e",Toast.LENGTH_SHORT).show()
        }
    }

}
fun setSharedPreferences() {
    sharedPreferences.edit()
        .putString("name", etNameProfile.text.toString()).apply()
    sharedPreferences.edit()
        .putString("mobile", etMobileProfile.text.toString()).apply()
   // sharedPreferences.edit().putString("handle", etHandleProfile.text.toString()).apply()
    sharedPreferences.edit().putString("description",etDescriptionProfile.text.toString()).apply()


}
}