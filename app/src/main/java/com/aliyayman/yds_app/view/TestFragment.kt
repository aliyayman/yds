package com.aliyayman.yds_app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aliyayman.yds_app.R
import com.aliyayman.yds_app.adapter.WordAdapter
import com.aliyayman.yds_app.databinding.FragmentTestBinding
import com.aliyayman.yds_app.service.myDatabase
import com.aliyayman.yds_app.viewmodel.TestViewModel

class TestFragment : Fragment() {

    private lateinit var binding: FragmentTestBinding
    private lateinit var viewModel: TestViewModel
    private lateinit var buttonA: Button
    private lateinit var buttonB: Button
    private lateinit var buttonC: Button
    private lateinit var buttonD: Button
    private lateinit var textViewsoru: TextView
    private lateinit var textViewdogru: TextView
    private lateinit var textViewyanlis: TextView
    private lateinit var textViewkelime: TextView
    private lateinit var textViewAnswer: TextView


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

        val mlist = viewModel.init()
        println("testfragment:")
        println(mlist)


        //  soruYukle()



        binding.buttonA.setOnClickListener { cevapKontrol(buttonA) }
        binding.buttonB.setOnClickListener { cevapKontrol(buttonB) }
        binding.buttonC.setOnClickListener { cevapKontrol(buttonC) }
        binding.buttonD.setOnClickListener { cevapKontrol(buttonD) }
    }

    private fun soruYukle() {
        val (dogruSoru, seceneklerList) = viewModel.soruYuklee()
        println("seceneklerList:" + seceneklerList)

        binding.textViewsoru.text = "${viewModel.soruSayac + 1}. SORU"
        binding.textViewdogru.text = "Doğru:${viewModel.dogruSayac}"
        binding.textViewyanlis.text = "Yanlış:${viewModel.yanlisSayac}"
        binding.textViewkelime.text = dogruSoru.ing

        binding.buttonA.text = seceneklerList[0].tc
        binding.buttonB.text = seceneklerList[1].tc
        binding.buttonC.text = seceneklerList[2].tc
        binding.buttonD.text = seceneklerList[3].tc
    }

    private fun cevapKontrol(button: Button) {
        val selectedAnswer = button.text.toString()
        val dogruMu = viewModel.dogruKontrol(selectedAnswer)

        if (dogruMu) {
            textViewAnswer.text = "Doğru Cevap!"
        } else {
            textViewAnswer.text = "Yanlış Cevap!"
        }

        if (viewModel.isQuizFinished()) {
            val (dogruSayac, yanlisSayac) = viewModel.getScores()
            println("sınav bitti")
            // Sınav bittiğinde yapılacak işlemler
        } else {
            viewModel.nextQuestion()
            soruYukle()
        }
    }
}