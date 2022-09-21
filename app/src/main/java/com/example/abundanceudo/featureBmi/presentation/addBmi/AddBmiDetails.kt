package com.example.abundanceudo.featureBmi.presentation.addBmi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abundanceudo.R
import com.example.abundanceudo.databinding.FragmentAddBmiDetailsBinding
import com.example.abundanceudo.featureBmi.data.repository.AdDismissErrorHandler
import com.example.abundanceudo.featureBmi.domain.model.BmiData
import com.example.abundanceudo.featureBmi.presentation.addBmi.viewmodel.AddBmiDetailEvent
import com.example.abundanceudo.featureBmi.presentation.addBmi.viewmodel.AddBmiViewModel
import com.example.abundanceudo.featureBmi.presentation.addBmi.viewmodel.BmiAdsEvent
import com.example.abundanceudo.featureBmi.presentation.util.genderData
import com.example.abundanceudo.featureBmi.presentation.util.onTextChanged
import com.example.abundanceudo.featureBmi.presentation.util.randomRangeData
import com.example.abundanceudo.featureBmi.presentation.util.scrollLinearOffset
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddBmiDetails : Fragment() {
    private var _binding: FragmentAddBmiDetailsBinding? = null
    private val binding get() = _binding!!
    private val bmiViewModel by viewModels<AddBmiViewModel>()
    private var isGoToResult = false
    private lateinit var bmiResult: BmiData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBmiDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()

        bmiViewModel.event.flowWithLifecycle(lifecycle)
            .onEach {
                if (it is AddBmiViewModel.UiEvent.GotoResults) {
                    showAd()
                    isGoToResult = true
                } else if (it is AddBmiViewModel.UiEvent.ShowSnackBar) {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
            .launchIn(lifecycleScope)

        bmiViewModel.bmiData.flowWithLifecycle(lifecycle)
            .onEach { bmiResult = it }
            .launchIn(lifecycleScope)

        bmiViewModel.onAdsEvent(
            BmiAdsEvent.AddFailedOrDismissed(object : AdDismissErrorHandler {
                override fun onAdDismissOrError() {
                    // bug fix for illegal state
                    val destinationIsHome = findNavController()
                        .currentDestination == findNavController().findDestination(R.id.addBmiDetails)
                    // isGoToResult helps prevent ad flickering
                    if (destinationIsHome && isGoToResult) {
                        val action =
                            AddBmiDetailsDirections.actionAddBmiDetailsToBmiDetails(bmiResult)
                        findNavController().navigate(action)
                    }
                }
            })
        )
    }

    private fun showAd() {
        bmiViewModel.onAdsEvent(
            BmiAdsEvent.ShowAd { interstitial ->
                interstitial.show(requireActivity())
            }
        )
    }

    private fun setUpViews() {
        binding.btnCalculate.setOnClickListener {
            bmiViewModel.onEvent(AddBmiDetailEvent.CalculateBmi)
        }
        binding.tvName.onTextChanged { bmiViewModel.onEvent(AddBmiDetailEvent.EnteredName(it)) }

        val weightPickerAdapter = CommonAdapter { value, view ->
            binding.weightPicker.scrollLinearOffset(view)
            bmiViewModel.onEvent(AddBmiDetailEvent.SelectedWeight(value.toDouble()))
        }

        val heightPickerAdapter = CommonAdapter { value, view ->
            binding.heightPicker.scrollLinearOffset(view)
            bmiViewModel.onEvent(AddBmiDetailEvent.SelectedHeight(value.toDouble()))
        }
        val genderPickerAdapter = CommonAdapter { _, view ->
            binding.genderPicker.scrollLinearOffset(view)
        }
        weightPickerAdapter.differ.submitList(randomRangeData)
        heightPickerAdapter.differ.submitList(randomRangeData)
        genderPickerAdapter.differ.submitList(genderData)

        // default value is needed in case user clicks on cal button before selecting from picker
        if (weightPickerAdapter.differ.currentList.isNotEmpty()) {
            bmiViewModel.onEvent(
                AddBmiDetailEvent.SelectedWeight(
                    weightPickerAdapter.getSelectedItem().toDouble()
                )
            )
        }
        if (heightPickerAdapter.differ.currentList.isNotEmpty()) {
            bmiViewModel.onEvent(
                AddBmiDetailEvent.SelectedHeight(
                    weightPickerAdapter.getSelectedItem().toDouble()
                )
            )
        }

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
