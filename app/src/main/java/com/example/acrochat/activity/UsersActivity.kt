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

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.acrochat.R
import com.example.acrochat.adapter.userAdapter
import com.example.acrochat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_users.*

class UsersActivity : AppCompatActivity() {
    var userList = ArrayList<User>()
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        userRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        acroUserLayout.setOnClickListener {
            val intent = Intent(this, AcroChatActivity::class.java)
            startActivity(intent)
        }

        usersBackBtn.setOnClickListener {
            onBackPressed()
        }


        getUsersList()

    }

    fun getUsersList(){
        userProgressBar.visibility = View.VISIBLE
        val firebase:FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                val currentUser = snapshot.getValue(User::class.java)
                if(currentUser!!.ProfileImage == ""){
                    imgProfile.setImageResource(R.drawable.ic_default_profile_img)
                }else{
                    Glide.with(this@UsersActivity).load(currentUser.ProfileImage).into(imgProfile)
                }

                for(dataSnapShot: DataSnapshot in snapshot.children){
                    val user = dataSnapShot.getValue(User::class.java)
                    if(user!!.userId != firebase.uid){
                        userList.add(user)
                    }
                }
                val userAdapter = userAdapter(this@UsersActivity, userList)

                userProgressBar.visibility = View.GONE

                userRecyclerView.adapter = userAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}