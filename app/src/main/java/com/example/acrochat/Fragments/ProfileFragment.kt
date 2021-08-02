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


package com.example.acrochat.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.acrochat.R
import com.example.acrochat.activity.ProfileActivity
import com.example.acrochat.activity.StartActivity
import com.example.acrochat.adapter.PostAdapter
import com.example.acrochat.model.Post
import com.example.acrochat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.util.*


class ProfileFragment : Fragment() {

    private lateinit var firebaseuser: FirebaseUser
    private lateinit var databaseRef: DatabaseReference



    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_profile, container, false)

        firebaseuser = FirebaseAuth.getInstance().currentUser!!
        databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseuser.uid)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        v.yourPostRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        v.profileProgressBarr.visibility = View.VISIBLE

        databaseRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                profileUserName.setText(user!!.userName)

                if(user.ProfileImage == ""){
                    userImage.setImageResource(R.drawable.ic_default_profile_img)
                }else{
                    Glide.with(this@ProfileFragment).load(user.ProfileImage).into(userImage)
                }
                profileProgressBarr.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })


        v.btnLogout.setOnClickListener {
            logout()
        }
        v.messageBtnFrag.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            startActivity(intent)
        }


        return v
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(activity, StartActivity::class.java)
        startActivity(intent)
    }


}