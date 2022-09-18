package com.example.abundanceudo.feature_bmi.presentation.add_bmi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abundanceudo.R
import com.example.abundanceudo.databinding.FragmentAddBmiDetailsBinding
import com.example.abundanceudo.feature_bmi.data.repository.AdDismissErrorHandler
import com.example.abundanceudo.feature_bmi.presentation.shared_viewmodels.AdsViewModel
import com.example.abundanceudo.feature_bmi.presentation.shared_viewmodels.BmiAdsEvent
import com.example.abundanceudo.feature_bmi.presentation.shared_viewmodels.BmiSharedViewModel
import com.example.abundanceudo.feature_bmi.presentation.util.genderData
import com.example.abundanceudo.feature_bmi.presentation.util.onTextChanged
import com.example.abundanceudo.feature_bmi.presentation.util.randomRangeData
import com.example.abundanceudo.feature_bmi.presentation.util.scrollLinearOffset
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddBmiDetails : Fragment() {
    private var _binding: FragmentAddBmiDetailsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel by activityViewModels<BmiSharedViewModel>()
    private val adsViewModel by activityViewModels<AdsViewModel>()
    private var isGoToResult = false

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

        sharedViewModel.event.flowWithLifecycle(lifecycle)
            .onEach {
                if (it is BmiSharedViewModel.UiEvent.GotoResults) {
                    showAd()
                    isGoToResult = true
                } else if (it is BmiSharedViewModel.UiEvent.ShowSnackBar) {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
            .launchIn(lifecycleScope)

        adsViewModel.onEvent(
            BmiAdsEvent.AddFailedOrDismissed(object : AdDismissErrorHandler {
                override fun onAdDismissOrError() {
                    // bug fix for illegal state
                    val destinationIsHome = findNavController()
                        .currentDestination == findNavController().findDestination(R.id.addBmiDetails)
                    if (destinationIsHome && isGoToResult) {
                        findNavController().navigate(AddBmiDetailsDirections.actionAddBmiDetailsToBmiDetails())
                    }
                }
            })
        )
    }

    private fun showAd() {
        adsViewModel.onEvent(
            BmiAdsEvent.ShowAd { interstitial ->
                interstitial.show(requireActivity())
            }
        )
    }

    private fun setUpViews() {
        binding.btnCalculate.setOnClickListener {
            sharedViewModel.onEvent(AddBmiDetailEvent.CalculateBmi)
        }
        binding.tvName.onTextChanged { sharedViewModel.onEvent(AddBmiDetailEvent.EnteredName(it)) }

        val weightPickerAdapter = CommonAdapter { value, view ->
            binding.weightPicker.scrollLinearOffset(view)
            sharedViewModel.onEvent(AddBmiDetailEvent.SelectedWeight(value.toDouble()))
        }

        val heightPickerAdapter = CommonAdapter { value, view ->
            binding.heightPicker.scrollLinearOffset(view)
            sharedViewModel.onEvent(AddBmiDetailEvent.SelectedHeight(value.toDouble()))
        }
        val genderPickerAdapter = CommonAdapter { _, view ->
            binding.genderPicker.scrollLinearOffset(view)
        }
        weightPickerAdapter.differ.submitList(randomRangeData)
        heightPickerAdapter.differ.submitList(randomRangeData)
        genderPickerAdapter.differ.submitList(genderData)

        // default value is needed in case user clicks on cal button before selecting from picker
        if (weightPickerAdapter.differ.currentList.isNotEmpty()) {
            sharedViewModel.onEvent(
                AddBmiDetailEvent.SelectedWeight(
                    weightPickerAdapter.getSelectedItem().toDouble()
                )
            )
        }
        if (heightPickerAdapter.differ.currentList.isNotEmpty()) {
            sharedViewModel.onEvent(
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
