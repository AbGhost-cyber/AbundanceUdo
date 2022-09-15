package com.example.abundanceudo.feature_bmi.presentation.bmi

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.abundanceudo.feature_bmi.domain.use_case.BmiUseCases
import javax.inject.Inject

class BmiDetailViewModel @Inject constructor(
    private val bmiUseCases: BmiUseCases
) : ViewModel()
