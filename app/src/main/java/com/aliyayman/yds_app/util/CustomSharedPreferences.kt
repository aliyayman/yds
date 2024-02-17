package com.aliyayman.yds_app.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class CustomSharedPreferences {
    private val PREFERENCES_TIME = "time"

    companion object {
        private var sharedPreferences: SharedPreferences? = null
        @Volatile
        private var instance: CustomSharedPreferences? = null

        private var lock = Any()
        operator fun invoke(context: Context): CustomSharedPreferences = instance ?: synchronized(
            lock
        ) {
            instance ?: makeCustomSharedPref(context).also {
                instance = it
            }
        }

        private fun makeCustomSharedPref(context: Context): CustomSharedPreferences {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return CustomSharedPreferences()
        }
    }

    fun saveTime(time: Long) {
        sharedPreferences?.edit(commit = true) {
            putLong(PREFERENCES_TIME, time)
        }
    }

    fun getTime() = sharedPreferences?.getLong(PREFERENCES_TIME, 0)
}