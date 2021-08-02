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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.acrochat.R
import com.example.acrochat.activity.CreatePostActivity
import com.example.acrochat.adapter.PostAdapter
import com.example.acrochat.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {
    var firebaseUser : FirebaseUser? = null
    var postList = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser

        v.postRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        v.addPost.setOnClickListener {
            val intent = Intent(context, CreatePostActivity::class.java)
            startActivity(intent)
        }

        v.postProgressBar.visibility = View.VISIBLE
        showPost()

        return v
    }


    private fun showPost(){
        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Posts")
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()
                for(dataSnapShot: DataSnapshot in snapshot.children){
                    val post = dataSnapShot.getValue(Post::class.java)
                        postList.add(post!!)
                }
                val postadapter = PostAdapter(postList)

                postRecyclerView.adapter = postadapter

                postProgressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}