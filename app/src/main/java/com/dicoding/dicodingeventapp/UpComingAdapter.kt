package com.dicoding.dicodingeventapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingeventapp.data.response.ListEventsItem

class UpComingAdapter(private val listReview: List<ListEventsItem>, private val onClick: (Int) -> Unit) : RecyclerView.Adapter<UpComingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_upcoming, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val upComing = listReview[position]
//        viewHolder.upComingDesc.text = HtmlCompat.fromHtml(
//            upComing.description.toString(),
//            HtmlCompat.FROM_HTML_MODE_LEGACY
//
//        )
        viewHolder.upComingName.text = upComing.name
        viewHolder.upComingSummary.text = upComing.summary
        Glide.with(viewHolder.itemView.context)
            .load(upComing.mediaCover)
            .into(viewHolder.imgUpComing)

        viewHolder.itemView.setOnClickListener{
            if (upComing.id != null) {
                onClick(upComing.id) // Pass the ID of the clicked item
            }
        }
    }

    override fun getItemCount(): Int {
        return listReview.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val upComingName: TextView = view.findViewById(R.id.name_upComing)
        val upComingSummary: TextView = view.findViewById(R.id.summaryUpcoming)
        val imgUpComing: ImageView = view.findViewById(R.id.upcoming_image)

    }

}