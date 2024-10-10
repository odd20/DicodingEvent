package com.dicoding.dicodingeventapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingeventapp.data.response.ListEventsItem

class FinishedAdapter(private val listReview: List<ListEventsItem>, private val onClick: (Int) -> Unit): RecyclerView.Adapter<FinishedAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val finishedName: TextView = view.findViewById(R.id.name_finished)
        val finishedSummary: TextView = view.findViewById(R.id.summaryFinished)
        val imgFinished: ImageView = view.findViewById(R.id.finished_image)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FinishedAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_finished, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val finished = listReview[position]
        viewHolder.finishedName.text = finished.name
        viewHolder.finishedSummary.text = finished.summary
        Glide.with(viewHolder.itemView.context)
            .load(finished.mediaCover)
            .into(viewHolder.imgFinished)

        viewHolder.itemView.setOnClickListener{
            if(finished.id != null){
                onClick(finished.id)
            }
        }

    }

    override fun getItemCount(): Int {
        return listReview.size
    }
}