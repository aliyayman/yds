package com.aliyayman.yds_app.viewmodel

import android.app.Application

class ResultViewModel(application: Application) : BaseViewModel(application) {
    fun checkResult(correctNumber: Int): String {
        return when (correctNumber) {
            10 -> "You're a genius!"
            8, 9 -> "You did great!"
            6, 7 -> "You're doing well!"
            else -> "You can do better!"
        }
    }
    fun writeMessage(correctNumber: Int) :String{
        val wrongNumber = 10 - correctNumber
        return "Correct:$correctNumber     Mistake:$wrongNumber"

    }
    fun calculateRate(correctNumber: Int) :String{
        val rate = correctNumber * 10
        return "%$rate SUCCESS"
    }


}