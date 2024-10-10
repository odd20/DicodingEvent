package com.dicoding.dicodingeventapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.dicoding.dicodingeventapp.R
import com.dicoding.dicodingeventapp.data.response.DetailResponse
import com.dicoding.dicodingeventapp.data.response.Event
import com.dicoding.dicodingeventapp.data.response.EventResponse
import com.dicoding.dicodingeventapp.data.response.ListEventsItem
import com.dicoding.dicodingeventapp.data.retrofit.ApiConfig
import com.dicoding.dicodingeventapp.databinding.ActivityDetailEventBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class DetailEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra("EVENT_ID", -1)
        Log.d("DetailEventActivity", "Event ID: $eventId") // Log ID yang diterima
        if (eventId != -1) {
            loadEventDetails(eventId)
        } else {
            Toast.makeText(this, "Invalid Event ID", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadEventDetails(eventId: Int) {
        showLoading(true)
        val client = ApiConfig.getApiService().getDetailEvent(eventId.toString())
        client.enqueue(object : Callback<DetailResponse>{
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error!!) {
                        val eventDetail = responseBody.event
                        if (eventDetail != null) {
                            setDetailEventData(eventDetail)
                        } else {
                            Toast.makeText(
                                this@DetailEventActivity,
                                "Event tidak ditemukan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@DetailEventActivity,
                            "No events found or an error occurred.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        } )
    }

    private fun setDetailEventData(eventDetail: Event) {
        binding.detailName.text = eventDetail.name ?: "Name not available"
        Glide.with(this)
            .load(eventDetail.mediaCover)
            .into(binding.mediaCover)
        binding.category.text = eventDetail.category
        val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // Format asli dari endTime
        val desiredFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) // Format yang diinginkan
        try {
            val date =
                eventDetail.endTime?.let { originalFormat.parse(it) }
            val formattedDate = desiredFormat.format(date) //
            binding.endTime.text =  formattedDate
        } catch (e: Exception) {
            binding.endTime.text = "Waktu tidak tersedia"
        }
        binding.ownerName.text = "Diselenggarakan Oleh: ${eventDetail.ownerName}"
        binding.quota.text = (eventDetail.quota?.minus(eventDetail.registrants!!)).toString()
        binding.descriptionEvent.text = HtmlCompat.fromHtml(
            eventDetail.description.toString(),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(eventDetail.link)
            startActivity(intent)
        }



    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }



}
