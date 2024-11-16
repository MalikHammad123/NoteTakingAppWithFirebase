package com.example.noteapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp.databinding.ActivityOnBoardingBinding


class OnBoardingActivity : AppCompatActivity() {
    var binding: ActivityOnBoardingBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()



    }
}