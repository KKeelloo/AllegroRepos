package com.example.allegrorepos

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class NameHolder (
    @JsonAlias("login", "title")
    var name: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NameHolderHolder(
    @JsonAlias("user")
    var owner: NameHolder
)