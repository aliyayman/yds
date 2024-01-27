package com.aliyayman.yds_app.data

import com.aliyayman.yds_app.R
import com.google.firebase.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig


class RemoteConfig {
    private lateinit var remoteConfig: FirebaseRemoteConfig

    fun getInitial(): FirebaseRemoteConfig{
        remoteConfig = com.google.firebase.ktx.Firebase.remoteConfig
        val configSettings = com.google.firebase.remoteconfig.ktx.remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.firebase_remote_config)
        return  remoteConfig
    }

    private val firebaseRemoteConfig: FirebaseRemoteConfig by lazy {
        val builder = FirebaseRemoteConfigSettings.Builder()
        builder.minimumFetchIntervalInSeconds = (if (BuildConfig.DEBUG) 10 else 3600)
        FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(builder.build())
            setDefaultsAsync(R.xml.firebase_remote_config)
        }
    }


    init {
        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseRemoteConfig.fetchAndActivate()
                }
            }




    }




}