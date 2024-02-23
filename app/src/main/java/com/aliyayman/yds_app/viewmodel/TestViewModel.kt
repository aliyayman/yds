package com.aliyayman.yds_app.viewmodel

import android.app.Application
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.repository.WordRepository
import com.aliyayman.yds_app.service.myDatabase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    application: Application,
    private val repository: WordRepository
    ) : BaseViewModel(application) {

    private lateinit var questionList: ArrayList<Word>
    private lateinit var mistakeList: ArrayList<Word>
    lateinit var correctQuestion: Word
    var questionCounter = 0
    var mistakeCounter = 0
    var correctCounter = 0
    private val optionRandomList = HashSet<Word>()
    private val optionList = ArrayList<Word>()
    private val exceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        println("Error:${throwable.localizedMessage}")
    }

    suspend fun init() {
        withContext(Dispatchers.IO + exceptionHandler) {
            questionList =
                repository.getWordTen().toList() as ArrayList<Word>
            correctQuestion = questionList[questionCounter]
            mistakeList = repository.getFaultWord3(correctQuestion.id)
                .toList() as ArrayList<Word>
        }
    }
    fun getQuestion(): Pair<Word, List<Word>> {
        optionRandomList.clear()
        optionRandomList.add(correctQuestion)
        optionRandomList.addAll(mistakeList)
        optionList.clear()
        optionList.addAll(optionRandomList)
        return Pair(correctQuestion, optionList)
    }
    fun checkCorrect(selectedAnswer: String): Boolean {
        val correctAnswer = correctQuestion.tc
        if (selectedAnswer == correctAnswer) {
            correctCounter++
            return true
        }
        mistakeCounter++
        return false
    }
    fun nextQuestion() {
        questionCounter++
    }
    fun isQuizFinished(): Boolean {
        return questionCounter == 9
    }
}