package com.dicoding.dicodingeventapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingeventapp.FinishedAdapter
import com.dicoding.dicodingeventapp.R
import com.dicoding.dicodingeventapp.UpComingAdapter
import com.dicoding.dicodingeventapp.data.response.EventResponse
import com.dicoding.dicodingeventapp.data.response.ListEventsItem
import com.dicoding.dicodingeventapp.data.retrofit.ApiConfig
import com.dicoding.dicodingeventapp.databinding.FragmentHomeBinding
import com.dicoding.dicodingeventapp.ui.DetailEventActivity
import com.dicoding.dicodingeventapp.ui.upcoming.UpComingFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.listFinishedHome.layoutManager = layoutManager
        binding.listUpComingHome.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        with(binding) {

        }
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
        }
        getFinishedEventHome()
        getUpComingEventHome()
        return binding.root
    }



    private fun getFinishedEventHome() {
        showLoading(true)
        val client = ApiConfig.getApiService().getFinishedEvents()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val latestEvents = responseBody.listEvents.take(5)
                        setFinishedData(latestEvents)
                    } else {
                        Toast.makeText(activity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(activity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getUpComingEventHome() {
        showLoading(true)
        val client = ApiConfig.getApiService().getActiveEvents()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.listEvents.isNotEmpty()) {

                        val firstEvent = responseBody.listEvents.first()


                        val upcomingEvents = List(5) { firstEvent }

                        setUpcomingData(upcomingEvents)
                    } else {
                        Toast.makeText(activity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(activity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun setFinishedData(finished: List<ListEventsItem>){
        val adapter = FinishedAdapter(finished){ eventId ->
            toDetailEvent(eventId)
        }
        binding.listFinishedHome.adapter = adapter
    }

    private fun setUpcomingData(upcoming: List<ListEventsItem>) {
        val adapter = UpComingAdapter(upcoming) { eventId ->
            toDetailEvent(eventId)
        }
        binding.listUpComingHome.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun toDetailEvent(eventId: Int){
        val intent = Intent(requireContext(), DetailEventActivity::class.java)
        intent.putExtra("EVENT_ID",eventId )
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}