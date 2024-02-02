package com.aliyayman.yds_app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.aliyayman.yds_app.databinding.FragmentResultBinding
import com.aliyayman.yds_app.viewmodel.ResultViewModel

class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding
    val args: ResultFragmentArgs by navArgs<ResultFragmentArgs>()
    private var correctNumber: Int = 0
    private lateinit var viewModel: ResultViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ResultViewModel::class.java)

        arguments?.let {
            correctNumber = args.CorrectNumber
        }
        with(binding) {
            textViewMessage.text = viewModel.checkResult(correctNumber)
            textViewRate.text = viewModel.calculateRate(correctNumber)
            textViewResult.text = viewModel.writeMessage(correctNumber)
            buttonRestart.setOnClickListener {
                it.findNavController()
                    .navigate(ResultFragmentDirections.actionResultFragmentToTestragment(1))
            }
        }
    }
}