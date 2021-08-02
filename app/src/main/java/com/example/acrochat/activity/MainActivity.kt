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
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.acrochat.Fragments.*
import com.example.acrochat.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*


class MainActivity : AppCompatActivity() {

    private var selectorFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, UsersActivity::class.java)
            startActivity(intent)
        }



        bottom_navigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navHome -> selectorFragment = HomeFragment()
                R.id.navSearch -> selectorFragment = SearchFragment()
                R.id.navBell -> selectorFragment = NotificationFragment()
                R.id.navProfile -> selectorFragment = ProfileFragment()
            }
            if (selectorFragment != null) {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    selectorFragment!!
                ).commit()
            }
            true
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment())
            .commit()
    }
    }


