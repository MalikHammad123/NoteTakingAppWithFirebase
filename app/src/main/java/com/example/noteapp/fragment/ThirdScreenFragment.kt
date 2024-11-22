package com.example.noteapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.noteapp.LoginActivity
import com.example.noteapp.SignUpActivity
import com.example.noteapp.databinding.FragmentThirdScreenBinding

class ThirdScreenFragment : Fragment() {
    var binding: FragmentThirdScreenBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentThirdScreenBinding.inflate(inflater, container, false)

        binding?.finish?.setOnClickListener {

            requireActivity().startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }
        return binding?.root
    }

}