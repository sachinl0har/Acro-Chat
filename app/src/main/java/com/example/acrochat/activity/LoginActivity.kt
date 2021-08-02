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
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            loginProgressBar.visibility = View.VISIBLE
            val email = enteremail.text.toString()
            val pwd = enterpassword.text.toString()

            if(TextUtils.isEmpty(email) && TextUtils.isEmpty(pwd)){
                Toast.makeText(applicationContext, "Invalid Email or password", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(this){
                    if(it.isSuccessful){
                        enteremail.setText("")
                        enterpassword.setText("")
                        loginProgressBar.visibility = View.GONE
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(applicationContext, "Invalid Email or password", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        textViewRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}