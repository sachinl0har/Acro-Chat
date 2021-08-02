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


package com.example.acrochat.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.acrochat.R
import com.example.acrochat.activity.ChatActivity
import com.example.acrochat.activity.UsersActivity
import com.example.acrochat.model.User
import de.hdodenhof.circleimageview.CircleImageView

class userAdapter(private val context: UsersActivity, private val userList: ArrayList<User>) :
    RecyclerView.Adapter<userAdapter.ViewHolder>() {

        class ViewHolder(view: View):RecyclerView.ViewHolder(view){
            val textUserName : TextView = view.findViewById(R.id.userName)
            val textTemp : TextView = view.findViewById(R.id.temp)
            val imgUser : CircleImageView = view.findViewById(R.id.userImage)
            val userLayout : LinearLayout = view.findViewById(R.id.userLayout)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.textUserName.text = user.userName
        Glide.with(context).load(user.ProfileImage).placeholder(R.drawable.ic_default_profile_img).into(holder.imgUser)

        holder.userLayout.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("UserId", user.userId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }


}