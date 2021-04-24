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

class ChosenDetailsViewModel: ViewModel() {
    companion object{
        val mapOfPathValues = mapOf(
            4 to "stargazers",
            6 to "forks",
            7 to  "issues"
        )
    }

    var token: String? = null
    var username: String? = null

    private val _statusInfo = MutableLiveData<Int>()
    val statusInfo: LiveData<Int> get() = _statusInfo

    lateinit var repo: String
    var numOfDetails = 0

    private val _itemsToAdd = MutableLiveData<Array<String>>()
    val itemsToAdd: LiveData<Array<String>> get() = _itemsToAdd

    fun fetchDetailsOfDetail(detail: Int){
        viewModelScope.launch(Dispatchers.IO){
            var loadedDetails = 0
            try {
                while(loadedDetails<numOfDetails){
                    val url = URL("https://api.github.com/repos/allegro/$repo/${mapOfPathValues[detail]}?per_page=$PER_PAGE&page=${loadedDetails/ PER_PAGE + 1}")
                    val con: HttpsURLConnection = url.openConnection() as HttpsURLConnection
                    setCon(con, token, username)
                    when (con.responseCode) {
                        200 -> {
                            val jsonString = con.inputStream.bufferedReader().use(BufferedReader::readText)
                            con.disconnect()
                            val mapper = jacksonObjectMapper()
                            val tmp: Array<NameHolder> =  when(detail) {
                                    4, 7-> {
                                        mapper.readValue(jsonString) as Array<NameHolder>
                                    }
                                    6 -> {
                                        val t = mapper.readValue(jsonString) as Array<NameHolderHolder>
                                        Array(t.size){
                                            t[it].owner
                                        }
                                    }
                                    else -> {
                                        Array(0){
                                            NameHolder("")
                                        }
                                    }
                                }
                            val toPost: Array<String> = Array(tmp.size){
                                tmp[it].name
                            }
                            loadedDetails += toPost.size
                            _itemsToAdd.postValue(toPost)
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
                }
                return@launch
            }catch (e : IOException){
                _statusInfo.postValue(R.string.connection_bad)
                return@launch
            }
        }
    }
}