package com.aliyayman.yds_app.view

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.aliyayman.yds_app.R
import com.aliyayman.yds_app.util.UploadWorker
import java.util.concurrent.TimeUnit

class SplashFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uploadWorkRequest: WorkRequest =
            PeriodicWorkRequestBuilder<UploadWorker>(1, TimeUnit.DAYS)
                .build()
        WorkManager.getInstance(view.context)
            .enqueue(uploadWorkRequest)




        object : CountDownTimer(1600, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {

                val action = SplashFragmentDirections.actionSplashFragmentToCategoryFragment()
                Navigation.findNavController(view).navigate(action)


            }
        }.start()
    }

}

