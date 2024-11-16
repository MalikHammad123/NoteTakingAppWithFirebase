package com.example.noteapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentSecondScreenBinding

class SecondScreenFragment : Fragment() {

    var binding: FragmentSecondScreenBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSecondScreenBinding.inflate(inflater, container, false)

        val viewPager =  activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding?.next?.setOnClickListener {
            viewPager?.currentItem = 2
        }
        return binding?.root
    }

}