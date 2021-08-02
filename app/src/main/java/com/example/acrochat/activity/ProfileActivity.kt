/*
 * Copyright © sachinl0har
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.example.acrochat.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.acrochat.R
import com.example.acrochat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.activity_users.imgProfile
import kotlinx.android.synthetic.main.signup_activity.*
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class ProfileActivity : AppCompatActivity() {
    private lateinit var firebaseuser: FirebaseUser
    private lateinit var databaseRef: DatabaseReference

    private var filePath: Uri? = null

    private val PICK_IMAGE_REQUEST:Int = 2020

    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseuser = FirebaseAuth.getInstance().currentUser!!
        databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseuser.uid)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        databaseRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                profileUserName.setText(user!!.userName)

                if(user.ProfileImage == ""){
                    userImage.setImageResource(R.drawable.ic_default_profile_img)
                }else{
                    Glide.with(this@ProfileActivity).load(user.ProfileImage).into(userImage)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

        })

        backBtn.setOnClickListener {
           onBackPressed()
        }

        userImage.setOnClickListener {
            chooseImage()
        }

        btnSave.setOnClickListener {
            profileProgressBar.visibility = View.VISIBLE
            uploadImage()
        }


    }




    private fun chooseImage() {
        val intent: Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode != null) {
            filePath = data!!.data
            try {
                var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                userImage.setImageBitmap(bitmap)
                btnSave.visibility = View.VISIBLE
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage() {
        if (filePath != null) {

            var ref: StorageReference = storageRef.child("image/" + UUID.randomUUID().toString())
            ref.putFile(filePath!!)
                .addOnSuccessListener {

                    val hashMap:HashMap<String,String> = HashMap()
                    hashMap["userName"] = profileUserName.text.toString()
                    hashMap["ProfileImage"] = filePath.toString()
                    databaseRef.updateChildren(hashMap as Map<String, Any>)
                    profileProgressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, "Uploaded", Toast.LENGTH_SHORT).show()
                    btnSave.visibility = View.GONE
                }
                .addOnFailureListener {
                    profileProgressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, "Failed" + it.message, Toast.LENGTH_SHORT)
                        .show()

                }

        }else{
            profileProgressBar.visibility = View.VISIBLE
            val hashMap:HashMap<String,String> = HashMap()
            hashMap["userName"] = profileUserName.text.toString()
            databaseRef.updateChildren(hashMap as Map<String, Any>)
            profileProgressBar.visibility = View.GONE
            Toast.makeText(applicationContext, "Name Saved", Toast.LENGTH_SHORT).show()
            btnSave.visibility = View.GONE
        }
    }


}