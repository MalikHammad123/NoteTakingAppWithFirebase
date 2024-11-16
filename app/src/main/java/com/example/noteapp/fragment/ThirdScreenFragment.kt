package com.example.noteapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.noteapp.LoginActivity
import com.example.noteapp.MainActivity
import com.example.noteapp.databinding.FragmentThirdScreenBinding

class ThirdScreenFragment : Fragment() {
    var binding: FragmentThirdScreenBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentThirdScreenBinding.inflate(inflater, container, false)

        binding?.finish?.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                AppLocalizationActivity.sharedPreferences?.edit()?.putBoolean("isFirstTime", false)?.apply()
//            }
            requireActivity().startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }
        return binding?.root
    }

}