package com.dicoding.dicodingeventapp.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingeventapp.UpComingAdapter
import com.dicoding.dicodingeventapp.data.response.EventResponse
import com.dicoding.dicodingeventapp.data.response.ListEventsItem
import com.dicoding.dicodingeventapp.data.retrofit.ApiConfig
import com.dicoding.dicodingeventapp.databinding.FragmentUpcomingBinding
import com.dicoding.dicodingeventapp.ui.DetailEventActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpComingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.listUpcoming.layoutManager = layoutManager
        getUpcomingEvent()

        return binding.root
    }

    private fun getUpcomingEvent() {
        showLoading(true)
        val client = ApiConfig.getApiService().getActiveEvents()

        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUpComingData(responseBody.listEvents)
                    }
                } else {
                    Toast.makeText(activity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(activity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setUpComingData(upComing: List<ListEventsItem>) {
        val adapter = UpComingAdapter(upComing) { eventId ->
            toDetailEvent(eventId) // Pass the event ID to the detail function
        }
        binding.listUpcoming.adapter = adapter
    }



    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun toDetailEvent(eventId: Int){
        val intent = Intent(requireContext(),DetailEventActivity::class.java)
        intent.putExtra("EVENT_ID",eventId )
        startActivity(intent) // Start the DetailEventActivity
    }
}
