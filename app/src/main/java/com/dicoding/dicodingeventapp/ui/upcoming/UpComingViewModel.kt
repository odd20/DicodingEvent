package com.dicoding.dicodingeventapp.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UpComingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is UpComing Fragment"
    }
    val text: LiveData<String> = _text
}