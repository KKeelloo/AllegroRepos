package com.example.allegrorepos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.allegrorepos.databinding.ActivityRepoDetailsBinding

class RepoDetailsActivity : AppCompatActivity() {

    private val sharedInfoViewModel: SharedInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityRepoDetailsBinding>(this, R.layout.activity_repo_details)

        val extras = intent.extras
        sharedInfoViewModel.token = extras?.getString("token")
        sharedInfoViewModel.username = extras?.getString("username")
        sharedInfoViewModel.repo = extras?.getString("repo")
    }
}