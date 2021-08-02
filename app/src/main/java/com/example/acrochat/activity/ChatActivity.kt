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

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.acrochat.R
import com.example.acrochat.adapter.ChatAdapter
import com.example.acrochat.model.Chat
import com.example.acrochat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.activity_users.imgProfile

class ChatActivity : AppCompatActivity() {
    var firebaseUser : FirebaseUser? = null
    var dbReference : DatabaseReference? = null
    var chatList = ArrayList<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        var intent = getIntent()
        var userId = intent.getStringExtra("UserId")

        tvBackBtn.setOnClickListener {
            onBackPressed()
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser
        dbReference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)

        dbReference!!.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                tvUserName.text = user!!.userName
                if(user.ProfileImage == ""){
                    imgProfile.setImageResource(R.drawable.ic_default_profile_img)
                }else{
                    Glide.with(this@ChatActivity).load(user.ProfileImage).into(imgProfile)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        btnSendMessage.setOnClickListener {
            var message:String = etMessage.text.toString()
            if(message.isEmpty()){
                Toast.makeText(applicationContext, "Type a Message", Toast.LENGTH_SHORT).show()
                etMessage.setText("")
            }else{
                sendMsg(firebaseUser!!.uid, userId, message)
                etMessage.setText("")
            }
        }
        readMessage(firebaseUser!!.uid, userId)
    }

    private fun sendMsg(senderId:String, recieverId:String, message:String){
        var dbReference : DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var hashmap : HashMap<String, String> = HashMap()
        hashmap.put("senderId", senderId)
        hashmap.put("recieverId", recieverId)
        hashmap.put("message", message)

        dbReference!!.child("Chat").push().setValue(hashmap)
    }

    private fun readMessage(senderId: String, recieverId: String) {
        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Chat")
        databaseRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for(dataSnapShot: DataSnapshot in snapshot.children){
                    val chat = dataSnapShot.getValue(Chat::class.java)
                    if(chat!!.senderId == senderId && chat!!.recieverId == recieverId || chat!!.senderId == recieverId && chat!!.recieverId == senderId){
                        chatList.add(chat)
                    }
                }
                val chatAdapter = ChatAdapter(this@ChatActivity, chatList)

                chatRecyclerView.adapter = chatAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        }
}