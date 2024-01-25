package com.aliyayman.yds_app.data

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class RemoteConfig {


    /*private val firebaseRemoteConfig: FirebaseRemoteConfig by lazy {
        val builder = FirebaseRemoteConfigSettings.Builder()
        builder.minimumFetchIntervalInSeconds = (if (BuildConfig.DEBUG) 10 else 3600)
        FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(builder.build())
            setDefaultsAsync(R.xml.remote_config_defaults)
        }
    }


    init {
        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseRemoteConfig.fetchAndActivate()
                }
            }

            companion object {
         const val WEB_SITE_LINK_KEY = "web_site_link"
         const val CLEANER_MONETIZATION = "clenaer_monetization"
        }

        fun getCleanerMonetization(): CleanerMonetization {
        val json = firebaseRemoteConfig.getString(CLEANER_MONETIZATION)
        json.takeIf { it.isNotBlank() }
            ?.let {
                return Gson().jsonToObjectOrNull(json, CleanerMonetization::class.java)
                    .also { Timber.d("CLEANER_MONETIZATION 1: $it") }
                    ?: CleanerMonetization().also { Timber.d("CLEANER_MONETIZATION 2: $it") }
            } ?: kotlin.run {
            return CleanerMonetization().also { Timber.d("CLEANER_MONETIZATION 3: $it") }
        }
    }



            */
}