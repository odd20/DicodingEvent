package com.dicoding.dicodingeventapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingeventapp.FinishedHomeAdapter.ViewHolder
import com.dicoding.dicodingeventapp.data.response.ListEventsItem

class UpComingHomeAdapter(private val listReview: List<ListEventsItem>, private val onClick: (Int) -> Unit): RecyclerView.Adapter<UpComingHomeAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val UpComingNameHome: TextView = view.findViewById(R.id.name_upcoming_home)
        val imgUpComingHome: ImageView = view.findViewById(R.id.upcoming_image_home)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_upcoming_home, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val upcoming = listReview[position]
        viewHolder.UpComingNameHome.text = upcoming.name
        Glide.with(viewHolder.itemView.context)
            .load(upcoming.mediaCover)
            .into(viewHolder.imgUpComingHome)

        viewHolder.itemView.setOnClickListener{
            if(upcoming.id != null){
                onClick(upcoming.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return listReview.size
    }


}