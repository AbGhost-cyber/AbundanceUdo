package com.example.abundanceudo.feature_bmi.presentation.bmi_detail

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.abundanceudo.feature_bmi.presentation.util.ProgressDialog
import com.example.abundanceudo.feature_bmi.presentation.util.formatStringSizes
import com.example.abundanceudo.feature_bmi.presentation.util.getBitmapFromView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class BmiDetails : Fragment() {
    private var _binding: FragmentBmiDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<BmiSharedViewModel>()
    private val adsViewModel by activityViewModels<AdsViewModel>()
    private lateinit var progressDialog: Dialog

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
                    val text1 = it.bmiValue.toString().substringBefore(".")
                    val text2 = it.bmiValue.toString().substring(text1.length)

                    tvBmiValue.text = formatStringSizes(text1, text2)
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
                Log.d("TAG", "onViewCreated: native ad loaded")
                binding.nativeAdView.apply {
                    setStyles(style)
                    setNativeAd(nativeAd)
                }
            }
        )
    }

    private fun setUpViews() {
        progressDialog = ProgressDialog.progressDialog(requireContext(), binding.root)
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
                parent.setOnClickListener {
                    progressDialog.show()
                    saveBmiImageAndShare()
                }
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

    private fun saveBmiImageAndShare() {
        var processIsSucceed = false
        val processIsCompleted: Boolean
        val bitmap = getBitmapFromView(binding.layoutStats)
        val cachePath = File(
            requireActivity().cacheDir.path +
                File.separator +
                "screen_" +
                System.currentTimeMillis() +
                ".jpeg"
        )
        var outstream: FileOutputStream? = null
        try {
            outstream = FileOutputStream(cachePath)
            bitmap.compress(CompressFormat.JPEG, 100, outstream)
            outstream.flush()
            processIsSucceed = true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            outstream?.close()
            processIsCompleted = true
        }

        // simulate process delay
        viewLifecycleOwner.lifecycleScope.launch {
            delay(TimeUnit.MILLISECONDS.toMillis(2))
            if (processIsSucceed && processIsCompleted) {
                shareBmiImage(cachePath)
            } else {
                Toast.makeText(requireContext(), "share failed, please retry", Toast.LENGTH_SHORT)
                    .show()
            }
            progressDialog.dismiss()
        }
    }

    private fun shareBmiImage(cachePath: File) {
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
