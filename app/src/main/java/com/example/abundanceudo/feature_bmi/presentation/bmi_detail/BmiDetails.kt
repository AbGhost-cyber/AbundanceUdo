package com.example.abundanceudo.feature_bmi.presentation.bmi_detail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.abundanceudo.R
import com.example.abundanceudo.databinding.FragmentBmiDetailsBinding
import com.example.abundanceudo.feature_bmi.presentation.MainActivity
import com.example.abundanceudo.feature_bmi.presentation.shared_viewmodels.AdsViewModel
import com.example.abundanceudo.feature_bmi.presentation.shared_viewmodels.BmiAdsEvent
import com.example.abundanceudo.feature_bmi.presentation.shared_viewmodels.BmiSharedViewModel
import com.example.abundanceudo.feature_bmi.presentation.util.getBitmapFromView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class BmiDetails : Fragment() {
    private var _binding: FragmentBmiDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<BmiSharedViewModel>()
    private val adsViewModel by activityViewModels<AdsViewModel>()

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
        setUpViews()

        viewModel.bmiData
            .flowWithLifecycle(lifecycle)
            .onEach {
                binding.apply {
                    tvBmiValue.text = it.bmiValue.toString()
                    val nameText =
                        "Hello ${it.userName}, you are ${it.bmiCategory.javaClass.simpleName}"
                    tvNameText.text = nameText
                    tvExtraText.text = it.extraText
                    val pText = "Ponderal Index: ${it.ponderalIndex}kg/m3"
                    tvPIndex.text = pText
                }
            }
            .launchIn(lifecycleScope)

        adsViewModel.onEvent(
            BmiAdsEvent.OnNativeAdLoaded { nativeAd, style ->
                binding.nativeAdView.apply {
                    setStyles(style)
                    setNativeAd(nativeAd)
                }
            }
        )
    }

    private fun setUpViews() {
        binding.apply {
            rateLayout.apply {
                textView.text = getString(R.string.rate)
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.star
                    )
                )
                parent.setOnClickListener { rateApp() }
            }
            shareLayout.apply {
                textView.text = getString(R.string.share)
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.share
                    )
                )
                parent.setOnClickListener { shareBmiData() }
            }
        }
    }

    private fun rateApp() {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "market://details?id =${requireContext().packageName}"
                    )
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.play_store_uri) + requireContext().packageName)
                )
            )
        }
    }

    private fun shareBmiData() {
        val bitmap = getBitmapFromView(binding.layoutStats)
        val cachePath = File(requireActivity().cacheDir.path + File.separator + "My BMI result")
        try {
            cachePath.createNewFile()
            val outstream = FileOutputStream(cachePath)
            bitmap.compress(CompressFormat.JPEG, 100, outstream)
            outstream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/*"
        val imageUri = FileProvider.getUriForFile(
            requireActivity(),
            requireActivity().packageName + ".provider",
            cachePath
        )
        share.putExtra(Intent.EXTRA_STREAM, imageUri)
        startActivity(Intent.createChooser(share, "Share via"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
