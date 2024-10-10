package com.dicoding.dicodingeventapp.ui.finished

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingeventapp.FinishedAdapter
import com.dicoding.dicodingeventapp.UpComingAdapter
import com.dicoding.dicodingeventapp.data.response.EventResponse
import com.dicoding.dicodingeventapp.data.response.ListEventsItem
import com.dicoding.dicodingeventapp.data.retrofit.ApiConfig
import com.dicoding.dicodingeventapp.databinding.FragmentFinishedBinding
import com.dicoding.dicodingeventapp.ui.DetailEventActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val finishedViewModel =
            ViewModelProvider(this).get(FinishedViewModel::class.java)

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.listFinished.layoutManager = layoutManager

        getFinishedEvent()
        return binding.root
    }

    private fun getFinishedEvent(){
        showLoading(true)
        val client = ApiConfig.getApiService().getFinishedEvents()
        client.enqueue(object : Callback<EventResponse>{
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                showLoading(false)
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        setFinishedData(responseBody.listEvents)
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
        binding.listFinished.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun toDetailEvent(eventId: Int){
        val intent = Intent(requireContext(), DetailEventActivity::class.java)
        intent.putExtra("EVENT_ID",eventId )
        startActivity(intent) // Start the DetailEventActivity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}