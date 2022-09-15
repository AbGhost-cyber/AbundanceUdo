package com.example.abundanceudo.feature_bmi.presentation.add_bmi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abundanceudo.R
import com.example.abundanceudo.databinding.FragmentAddBmiDetailsBinding
import com.example.abundanceudo.feature_bmi.presentation.util.genderData
import com.example.abundanceudo.feature_bmi.presentation.util.onTextChanged
import com.example.abundanceudo.feature_bmi.presentation.util.randomRangeData
import com.example.abundanceudo.feature_bmi.presentation.util.scrollLinearOffset
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBmiDetails : Fragment() {
    private var _binding: FragmentAddBmiDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<AddBmiViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBmiDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.event.collect {
                if (it is AddBmiViewModel.UiEvent.SaveNote) {
                    findNavController().navigate(R.id.action_addBmiDetails_to_bmiDetails)
                } else if (it is AddBmiViewModel.UiEvent.ShowSnackBar) {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUpViews() {
        binding.btnCalculate.setOnClickListener {
            viewModel.onEvent(AddBmiDetailEvent.CalculateBmi)
        }
        binding.tvName.onTextChanged { viewModel.onEvent(AddBmiDetailEvent.EnteredName(it)) }

        val weightPickerAdapter = CommonAdapter { value, view ->
            binding.weightPicker.scrollLinearOffset(requireContext(), view)
            viewModel.onEvent(AddBmiDetailEvent.SelectedWeight(value.toDouble()))
        }
        // default value is needed in case user clicks on cal button before selecting from picker
        viewModel.onEvent(
            AddBmiDetailEvent.SelectedWeight(
                weightPickerAdapter.getSelectedItem().toDouble()
            )
        )
        viewModel.onEvent(
            AddBmiDetailEvent.SelectedHeight(
                weightPickerAdapter.getSelectedItem().toDouble()
            )
        )

        val heightPickerAdapter = CommonAdapter { value, view ->
            binding.heightPicker.scrollLinearOffset(requireContext(), view)
            viewModel.onEvent(AddBmiDetailEvent.SelectedHeight(value.toDouble()))
        }
        val genderPickerAdapter = CommonAdapter { _, view ->
            binding.genderPicker.scrollLinearOffset(requireContext(), view)
        }
        weightPickerAdapter.differ.submitList(randomRangeData)
        heightPickerAdapter.differ.submitList(randomRangeData)
        genderPickerAdapter.differ.submitList(genderData)

        binding.weightPicker.apply {
            adapter = weightPickerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.heightPicker.apply {
            adapter = heightPickerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.genderPicker.apply {
            adapter = genderPickerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
