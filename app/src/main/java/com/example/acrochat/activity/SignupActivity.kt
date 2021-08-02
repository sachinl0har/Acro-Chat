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
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.acrochat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.signup_activity.*

class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)

        auth = FirebaseAuth.getInstance()

        textViewLogin.setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        btnSignup.setOnClickListener{
            signupProgressBar.visibility = View.VISIBLE
            val userName = enterusername.text.toString()
            val email = enteremail.text.toString()
            val pwd = enterpassword.text.toString()
            val cpwd = entercpassword.text.toString()
            if(TextUtils.isEmpty(userName)){
                Toast.makeText(applicationContext, "Enter UserName", Toast.LENGTH_SHORT).show()
            }
            if(TextUtils.isEmpty(email)){
            Toast.makeText(applicationContext, "Enter Email", Toast.LENGTH_SHORT).show()
            }
            if(TextUtils.isEmpty(pwd)){
                Toast.makeText(applicationContext, "Enter Password", Toast.LENGTH_SHORT).show()
            }
            if(TextUtils.isEmpty(cpwd)){
                Toast.makeText(applicationContext, "Enter Password Again", Toast.LENGTH_SHORT).show()
            }
            if(pwd != cpwd){
                Toast.makeText(applicationContext, "Password Did not Match", Toast.LENGTH_SHORT).show()
            }
            registerUser(userName, email, pwd)
        }
    }

    private fun registerUser(userName:String, email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){
            val user: FirebaseUser? = auth.currentUser
            val userId: String = user!!.uid
            databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)

            val hashMap:HashMap<String, String> = HashMap()
            hashMap.put("userId", userId)
            hashMap.put("userName", userName)
            hashMap.put("ProfileImage", "")

            databaseRef.setValue(hashMap).addOnCompleteListener(this){
                if (it.isSuccessful){
                    enterusername.setText("")
                    enteremail.setText("")
                    enterpassword.setText("")
                    entercpassword.setText("")
                    signupProgressBar.visibility = View.GONE
//                    new Activity
                    val intent = Intent(this@SignupActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}