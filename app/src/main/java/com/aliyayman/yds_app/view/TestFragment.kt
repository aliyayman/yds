package com.aliyayman.yds_app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.aliyayman.yds_app.databinding.FragmentTestBinding
import com.aliyayman.yds_app.viewmodel.TestViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class TestFragment : Fragment(), CoroutineScope {
    private lateinit var binding: FragmentTestBinding
    private lateinit var viewModel: TestViewModel
    private val job = Job()
    private lateinit var mAdView : AdView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTestBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(TestViewModel::class.java)

        writeQuestion()

        binding.buttonA.setOnClickListener { checkAnswer(binding.buttonA, view) }
        binding.buttonB.setOnClickListener { checkAnswer(binding.buttonB, view) }
        binding.buttonC.setOnClickListener { checkAnswer(binding.buttonC, view) }
        binding.buttonD.setOnClickListener { checkAnswer(binding.buttonD, view) }


        //ca-app-pub-5875015425896746/2911685865
        MobileAds.initialize(requireContext()) {}
        mAdView = binding.adViewTest
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

    }

    private fun writeQuestion() {
        launch {
            viewModel.init()
            val (correctQuestion, optionsList) = viewModel.getQuestion()

            binding.textViewsoru.text = "${viewModel.questionCounter + 1}. QUESTION"
            binding.textViewCorrect.text = "Correct:${viewModel.correctCounter}"
            binding.textViewMistake.text = "Mistake:${viewModel.mistakeCounter}"
            binding.textViewWord.text = correctQuestion.ing

            binding.buttonA.text = optionsList[0].tc
            binding.buttonB.text = optionsList[1].tc
            binding.buttonC.text = optionsList[2].tc
            binding.buttonD.text = optionsList[3].tc
        }
    }

    private fun checkAnswer(button: Button, view: View) {
        val selectedAnswer = button.text.toString()
        val correctAnswer = viewModel.correctQuestion.tc
        val isCorrect = viewModel.checkCorrect(selectedAnswer)

        if (isCorrect) {
            binding.textViewAnswer.text = ""
        } else {
            binding.textViewAnswer.text = "Answer: $correctAnswer"
        }

        if (viewModel.isQuizFinished()) {
            val action =
                TestFragmentDirections.actionTestragmentToResultFragment(viewModel.correctCounter)
            Navigation.findNavController(view).navigate(action)

        } else {
            viewModel.nextQuestion()
            writeQuestion()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
}
