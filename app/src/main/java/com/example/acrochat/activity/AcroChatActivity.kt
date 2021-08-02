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

import android.accessibilityservice.GestureDescription
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.acrochat.MySingleton
import com.example.acrochat.R
import com.example.acrochat.adapter.AcroChatAdapter
import com.example.acrochat.model.AcroChat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_acro_chat.*
import kotlinx.android.synthetic.main.activity_chat.*
import java.net.URLEncoder

class AcroChatActivity : AppCompatActivity() {
    var firebaseUser : FirebaseUser? = null
    var acroChatList = ArrayList<AcroChat>()
    var USER_KEY = "user"
    var BOT_KEY = "bot"
    var mRequestQueue:RequestQueue? = null

    private var messageModalArrayList: ArrayList<AcroChat>? = null
    private val messageRVAdapter: AcroChatAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acro_chat)

//        acroChatRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


        firebaseUser = FirebaseAuth.getInstance().currentUser

        tvAcroBackBtn.setOnClickListener {
            onBackPressed()
        }

        messageModalArrayList = ArrayList()

        btnAcroSendMessage.setOnClickListener {
            acroUserMessage.text = etAcroMessage.text.toString()
            acroMessageProgressBar.visibility = View.VISIBLE
            var message:String = etAcroMessage.text.toString()
            if(message.isEmpty()){
                Toast.makeText(applicationContext, "Type a Message", Toast.LENGTH_SHORT).show()
                etAcroMessage.setText("")
            }else{
                acroSendMsg(firebaseUser!!.uid, message)
                etAcroMessage.setText("")
            }
        }
            acroReadMsg(firebaseUser!!.uid)
    }

    private fun acroSendMsg(senderId:String, message:String){
        messageModalArrayList!!.add(AcroChat(senderId, USER_KEY))
//        val url = "https://api.affiliateplus.xyz/api/chatbot?message=$message&botname=Acro&ownername=sachinl0har"
        val url = "http://api.brainshop.ai/get?bid=157984&key=3S0hhLXZ5GS2KYs4&uid=[uid]&msg=[$message]"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                acroMessageProgressBar.visibility = View.GONE
                val acroReply : String = response.toString()
                acroMessage.text = acroReply

                var dbReference : DatabaseReference? = FirebaseDatabase.getInstance().getReference()

                var hashmap : HashMap<String, String> = HashMap()
                hashmap.put("senderId", senderId)
                hashmap.put("message", message)
                hashmap.put("acroMessage", acroReply)

                dbReference!!.child("AcroChat").push().setValue(hashmap)

            }
        ) { error ->
            // TODO: Handle error
        }

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    private fun acroReadMsg(senderId: String){
        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("AcroChat")
        databaseRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                acroChatList.clear()
                for(dataSnapShot: DataSnapshot in snapshot.children){
                    val chat = dataSnapShot.getValue(AcroChat::class.java)

                    if (chat!!.senderId == senderId) {
                        acroChatList.add(chat)

                    }
                }
                val acrochatAdapter = AcroChatAdapter(this@AcroChatActivity, acroChatList)

//                acroChatRecyclerView.adapter = acrochatAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}