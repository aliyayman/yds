package com.aliyayman.yds_app.viewmodel

import android.app.Application
import com.aliyayman.yds_app.model.Word
import com.aliyayman.yds_app.service.myDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TestViewModel(application: Application) : BaseViewModel(application) {

    private lateinit var questionList: ArrayList<Word>
    private lateinit var mistakeList: ArrayList<Word>
    lateinit var correctQuestion: Word
    var questionCounter = 0
    var mistakeCounter = 0
    var correctCounter = 0
    private val optionRandomList = HashSet<Word>()
    private val optionList = ArrayList<Word>()

    suspend fun init() {
        withContext(Dispatchers.IO) {
            questionList =
                myDatabase(getApplication()).wordDao().getWordTen().toList() as ArrayList<Word>
            correctQuestion = questionList[questionCounter]
            mistakeList = myDatabase(getApplication()).wordDao().getFaultWord3(correctQuestion.id)
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