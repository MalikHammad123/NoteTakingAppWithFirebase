package com.example.noteapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.noteapp.adapter.ViewPagerAdapter
import com.example.noteapp.databinding.FragmentBoardingBinding

class BoardingFragment : Fragment() {
    private var _binding : FragmentBoardingBinding?=null
    private val binding : FragmentBoardingBinding? get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBoardingBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // findNavController().navigate(R.id.action_boardingFragment_to_viewPagerFragment)
        val fragmentList = arrayListOf<Fragment>(
            FirstScreenFragment(),
            SecondScreenFragment(),
            ThirdScreenFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,childFragmentManager,
            lifecycle
        )
        binding?.viewPager?.adapter = adapter
        binding?.dotsIndicator?.setViewPager2(binding?.viewPager!!)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}