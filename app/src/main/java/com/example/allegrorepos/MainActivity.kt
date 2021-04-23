package com.example.allegrorepos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.allegrorepos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNoToken.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        binding.btnToken.setOnClickListener {
            if(binding.textToken.text.isNotBlank() && binding.textLogin.text.isNotBlank()){
                intent = Intent(this, ListActivity::class.java)
                intent.putExtra("username", binding.textLogin.text.toString())
                intent.putExtra("token", binding.textToken.text.toString())
                startActivity(intent)
            }
            if(binding.textLogin.text.isBlank())
                Toast.makeText(this, resources.getText(R.string.toast_username), Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, resources.getText(R.string.toast_token), Toast.LENGTH_SHORT).show()
        }
    }
}