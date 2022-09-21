package com.example.abundanceudo.featureBmi.presentation.bmiDetail

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.abundanceudo.R
import com.example.abundanceudo.databinding.FragmentBmiDetailsBinding
import com.example.abundanceudo.featureBmi.presentation.MainActivity
import com.example.abundanceudo.featureBmi.presentation.addBmi.viewmodel.BmiAdsEvent
import com.example.abundanceudo.featureBmi.presentation.util.ProgressDialog
import com.example.abundanceudo.featureBmi.presentation.util.formatStringSizes
import com.example.abundanceudo.featureBmi.presentation.util.toBitmap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class BmiDetails : Fragment() {
    private var _binding: FragmentBmiDetailsBinding? = null
    private val binding get() = _binding!!
    private val bmiDetailsViewModel by viewModels<BmiDetailsViewModel>()
    private lateinit var progressDialog: Dialog
    private val args: BmiDetailsArgs by navArgs()

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

        with(args.bmiResult) {
            binding.apply {
                val text1 = bmiValue.toString().substringBefore(".")
                val text2 = bmiValue.toString().substring(text1.length)

                tvBmiValue.text = formatStringSizes(text1, text2)
                val nameText =
                    "Hello $userName, you are ${bmiCategory.javaClass.simpleName}"
                tvNameText.text = nameText
                tvExtraText.text = extraText
                val pText = "Ponderal Index: ${ponderalIndex}kg/m3"
                tvPIndex.text = pText
            }
        }

        bmiDetailsViewModel.onNativeAdLoaded(
            BmiAdsEvent.OnNativeAdLoaded { nativeAd, style ->
                Log.d("TAG", "onViewCreated: native ad loaded")
                binding.nativeAdView.apply {
                    setStyles(style)
                    setNativeAd(nativeAd)
                }
            }
        )
        bmiDetailsViewModel.event.flowWithLifecycle(lifecycle)
            .onEach {
                if (it is BmiDetailsViewModel.UiEvent.ShowSnackBar) {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
            .launchIn(lifecycleScope)

        bmiDetailsViewModel.cache.flowWithLifecycle(lifecycle)
            .onEach {
                it.file?.let { file ->
                    // simulate delay
                    delay(TimeUnit.MILLISECONDS.toMillis(3))
                    shareBmiImage(file)
                }
            }
            .launchIn(lifecycleScope)
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
        val bitmap = binding.layoutStats.toBitmap()
        val cacheFile = File(
            requireActivity().cacheDir.path +
                File.separator +
                "screen_" +
                System.currentTimeMillis() +
                ".jpeg"
        )
        bmiDetailsViewModel.onSharePressed(bitmap, cacheFile)
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
        progressDialog.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
