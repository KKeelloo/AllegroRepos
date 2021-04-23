package com.example.allegrorepos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailsViewModel: ViewModel() {

    private val _details = MutableLiveData<RepoDetailsHolder>()
    val details: LiveData<RepoDetailsHolder> get() = _details

    private val _statusInfo = MutableLiveData<Int>()
    val statusInfo: LiveData<Int> get() = _statusInfo

    fun fetchDetails(repo: String, username: String?, token: String?){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL("https://api.github.com/repos/allegro/$repo")
                val con: HttpsURLConnection = url.openConnection() as HttpsURLConnection
                setCon(con, token, username)
                when (con.responseCode) {
                    200 -> {
                        val jsonString = con.inputStream.bufferedReader().use(BufferedReader::readText)
                        con.disconnect()
                        val mapper = jacksonObjectMapper()
                        val tmp: RepoDetailsHolder = mapper.readValue(jsonString)
                        _details.postValue(tmp)
                        _statusInfo.postValue(R.string.loaded)
                        return@launch
                    }
                    403 -> {
                        _statusInfo.postValue(R.string.forbidden)
                        con.disconnect()
                        return@launch
                    }
                    404 -> {
                        _statusInfo.postValue(R.string.disappeared)
                        con.disconnect()
                        return@launch
                    }
                    else -> {
                        _statusInfo.postValue(R.string.unexpected)
                        con.disconnect()
                        return@launch
                    }
                }
            }catch (e : IOException){
                _statusInfo.postValue(R.string.connection_bad)
                return@launch
            }
        }
    }
}