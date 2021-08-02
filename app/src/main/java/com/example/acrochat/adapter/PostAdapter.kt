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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.acrochat.R
import com.example.acrochat.model.Post
import de.hdodenhof.circleimageview.CircleImageView

class PostAdapter(private val postList: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val textUserName : TextView = view.findViewById(R.id.userNamePost)
        val textPost : TextView = view.findViewById(R.id.inputPostText)
        val postTime : TextView = view.findViewById(R.id.postCurrentTime)
        val imgUser : CircleImageView = view.findViewById(R.id.userImagePost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = postList[position]
//        holder.textUserName.text = post.createdBy
        holder.textPost.text = post.postMessage
//        holder.postTime.text = post.createdAt
    }

    override fun getItemCount(): Int {
        return postList.size
    }

}

