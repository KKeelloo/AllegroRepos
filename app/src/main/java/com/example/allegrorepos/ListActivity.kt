package com.example.allegrorepos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.allegrorepos.databinding.ActivityListBinding

class ListActivity : AppCompatActivity(){
    private lateinit var binding: ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listView: ListView = binding.listRepos
        val extras = intent.extras
        listView.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        val model = ViewModelProvider(this,
            ListViewModelFactory(extras?.getString("username"),
                extras?.getString("token"))).get(ListViewModel::class.java)

        model.reposList.observe(this){ value ->
            value.let{
                @Suppress("UNCHECKED_CAST")
                (listView.adapter as ArrayAdapter<String>).addAll(*it)
            }
        }

        model.statusInfo.observe(this){ value ->
            value.let{
                Toast.makeText(this, getString(it), Toast.LENGTH_SHORT).show()
            }
        }

        listView.setOnItemClickListener{ _, _, position, _ ->
            val chosenRepo: String= listView.adapter.getItem(position) as String
            val intent = Intent(this, RepoDetailsActivity::class.java)
            intent.putExtra("repo", chosenRepo)
            intent.putExtra("username", extras?.getString("username"))
            intent.putExtra("token", extras?.getString("token"))
            startActivity(intent)
        }
    }
}