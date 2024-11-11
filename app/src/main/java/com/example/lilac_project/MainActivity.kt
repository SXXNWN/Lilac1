package com.example.lilac_project

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lilac_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAssignment.setOnClickListener {
            val fragment1 = AssignmentFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, fragment1)
                .commit()
        }

        binding.btnHomework.setOnClickListener {
            val fragment2 = HomeworkFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, fragment2)
                .commit()
        }

        binding.btnStudy.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, StudyFragment())
                .commit()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}