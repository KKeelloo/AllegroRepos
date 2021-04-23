package com.example.allegrorepos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedInfoViewModel: ViewModel() {
    var repo: String? =null

    private val _chosenDetail = MutableLiveData<Int>()
    val chosenDetail: LiveData<Int> get() = _chosenDetail

    private var _numberOfDetails = 0
    val numberOfDetails: Int get() = _numberOfDetails

    fun choseDetail(detail: Int, num: Int){
        _numberOfDetails = num
        _chosenDetail.value = detail
    }

    var username: String? = null
    var token: String? = null
}