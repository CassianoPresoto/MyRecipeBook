package com.example.myrecipebook.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _title = MutableLiveData("My Recipe Book")
    val title: LiveData<String> = _title
}
