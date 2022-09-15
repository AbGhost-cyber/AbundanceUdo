package com.example.abundanceudo.feature_bmi.presentation.bmi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.abundanceudo.R
import com.example.abundanceudo.databinding.FragmentBmiDetailsBinding
import com.example.abundanceudo.feature_bmi.presentation.MainActivity
import com.example.abundanceudo.feature_bmi.presentation.add_bmi.AddBmiViewModel

class BmiDetails : Fragment() {
    private var _binding: FragmentBmiDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<AddBmiViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBmiDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar
            ?.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.bmiData.collect {
                Log.d(
                    "TAG",
                    "onViewCreated: ${it.bmiValue}" +
                        " ${it.bmiCategory.javaClass.simpleName}"
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
