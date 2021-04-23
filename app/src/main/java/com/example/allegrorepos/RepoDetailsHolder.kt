package com.example.allegrorepos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class RepoDetailsHolder(val name: String?,
                             val description: String?,
                             val created_at: String?,
                             val size: String?,
                             val stargazers_count: String?,
                             val language: String?,
                             val forks_count: String?,
                             val open_issues: String?,
                             val default_branch: String?,
                             val archived: String?){

    fun fetchFromPosition(i: Int): String? {
        when(i){
            0 -> {
                return name
            }
            1 -> {
                return description
            }
            2 -> {
                return created_at
            }
            3 -> {
                return size
            }
            4 -> {
                return stargazers_count
            }
            5 -> {
                return language
            }
            6 -> {
                return forks_count
            }
            7 -> {
                return open_issues
            }
            8 -> {
                return default_branch
            }
            9 -> {
                return archived
            }
        }
        return ""
    }
}
