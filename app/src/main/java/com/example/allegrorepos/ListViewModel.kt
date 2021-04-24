package com.example.allegrorepos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

class ListViewModel(private val username: String?, private val token: String?): ViewModel() {

    private val _statusInfo = MutableLiveData<Int>()
    val statusInfo: LiveData<Int> get() = _statusInfo

    private val _reposList = MutableLiveData<Array<String>>()
    val reposList: LiveData<Array<String>> get() = _reposList

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var url = URL("https://api.github.com/users/allegro")
                var con: HttpsURLConnection = url.openConnection() as HttpsURLConnection
                setCon(con, token, username)
                var status = con.responseCode
                when (status) {
                    200 -> {
                        var jsonString = con.inputStream.bufferedReader().use(BufferedReader::readText)
                        val numOfRepos:Int = JSONObject(jsonString).get("public_repos") as Int
                        con.disconnect()
                        var pgCount = 0
                        val mapper = jacksonObjectMapper()
                        while(pgCount*PER_PAGE < numOfRepos){
                            url = URL("https://api.github.com/users/allegro/repos?per_page=$PER_PAGE&page=${pgCount+1}")
                            con = url.openConnection() as HttpsURLConnection
                            setCon(con, token, username)
                            status = con.responseCode
                            when (status) {
                                200 -> {
                                    jsonString = con.inputStream.bufferedReader().use(BufferedReader::readText)
                                    val tmpList: List<NameHolder> = mapper.readValue(jsonString)
                                    val stringArray = Array(tmpList.size) {
                                        tmpList[it].name
                                    }
                                    _reposList.postValue(stringArray)
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
                            pgCount++
                            con.disconnect()
                        }
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