package com.dicoding.dicodingeventapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingeventapp.FinishedAdapter.ViewHolder
import com.dicoding.dicodingeventapp.data.response.ListEventsItem

class FinishedHomeAdapter(private val listReview: List<ListEventsItem>, private val onClick: (Int) -> Unit): RecyclerView.Adapter<FinishedHomeAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val finishedNameHome: TextView = view.findViewById(R.id.name_finished_home)
        val finishedSummaryHome: TextView = view.findViewById(R.id.summaryFinishedHome)
        val imgFinishedHome: ImageView = view.findViewById(R.id.finished_image_home)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_finished_home, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listReview.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val finished = listReview[position]
        viewHolder.finishedNameHome.text = finished.name
        viewHolder.finishedSummaryHome.text = finished.summary
        Glide.with(viewHolder.itemView.context)
            .load(finished.mediaCover)
            .into(viewHolder.imgFinishedHome)

        viewHolder.itemView.setOnClickListener{
            if(finished.id != null){
                onClick(finished.id)
            }
        }
    }
}