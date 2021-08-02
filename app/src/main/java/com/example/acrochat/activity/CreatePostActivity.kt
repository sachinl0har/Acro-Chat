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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.acrochat.R
import com.example.acrochat.adapter.PostAdapter
import com.example.acrochat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_chat.imgProfile
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.post_item.*

class CreatePostActivity : AppCompatActivity() {

    val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    var dbReference : DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        postBackBtn.setOnClickListener {
            onBackPressed()
        }

        postBtn.setOnClickListener {
            var input:String = postInput.text.toString()
            if(input.isEmpty()){
                Toast.makeText(this, "Invalid Post", Toast.LENGTH_SHORT).show()
            }else{
                addPost(input, firebaseUser!!.uid)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun addPost(postText : String, postSenderId:String){
        val currentTime : Long = System.currentTimeMillis()

        var hashmap : HashMap<String, String> = HashMap()
        hashmap.put("postSenderId", postSenderId)
        hashmap.put("postMessage", postText)
        hashmap.put("createdBy", postSenderId)
        hashmap.put("createdAt", currentTime.toString())

        databaseRef!!.child("Posts").push().setValue(hashmap)

    }
}

