package com.example.allegrorepos

import java.util.*
import javax.net.ssl.HttpsURLConnection

const val PER_PAGE = 100

fun setCon(con: HttpsURLConnection, token: String?, username: String?){
    con.requestMethod = "GET"
    con.setRequestProperty("Accept", "application/vnd.github.v3+json")
    if (!token.isNullOrBlank() && !username.isNullOrBlank()) {
        val tmp = Base64.getEncoder().encodeToString(("$username:$token").toByteArray())
        con.setRequestProperty("Authorization", "Basic $tmp")
    }
    con.connectTimeout = 5000
    con.readTimeout = 5000
}