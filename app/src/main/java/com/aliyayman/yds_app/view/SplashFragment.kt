package com.aliyayman.yds_app.view

import android.content.Intent
import android.net.Uri
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
import com.aliyayman.yds_app.databinding.FragmentSplashBinding
import com.aliyayman.yds_app.util.UploadWorker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import java.util.concurrent.TimeUnit

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    private var isUpdate  = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uploadWorkRequest: WorkRequest =
            PeriodicWorkRequestBuilder<UploadWorker>(1, TimeUnit.DAYS)
                .build()
        WorkManager.getInstance(view.context)
            .enqueue(uploadWorkRequest)

           checkUpdate(view)
    }

    private fun checkVersion(view: View){
        if (isUpdate){
            Snackbar.make(view,"You need to update your application", Snackbar.LENGTH_INDEFINITE).show()

        }else{
            object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }
                override fun onFinish() {

                    val action = SplashFragmentDirections.actionSplashFragmentToCategoryFragment()
                    val  navController = view?.let { Navigation.findNavController(it) }
                    navController?.popBackStack(R.id.categoryFragment,false)
                    navController?.navigate(action)

                }
            }.start()
        }
    }

    private fun checkUpdate(view: View)  {
        firebaseRemoteConfig.setDefaultsAsync(R.xml.firebase_remote_config)
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
                if (it.isSuccessful){
                    val lastVersion = firebaseRemoteConfig.getDouble("last_version")
                    println("last version:$lastVersion")
                    val currentVersion = requireActivity().packageManager.getPackageInfo(requireActivity().packageName,0).versionName.toDouble()
                    println("current:$currentVersion")
                    if(currentVersion < lastVersion){
                        isUpdate = true
                        binding.updateButton.visibility = View.VISIBLE
                        binding.updateButton.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(PLAY_STORE_URL)
                            startActivity(intent)
                        }
                    }
                    checkVersion(view)
                }
            else if (!it.isSuccessful){
                    Snackbar.make(view,"Check Your Internet Connection ", Snackbar.LENGTH_INDEFINITE).show()
                }
            }
    }

    companion object{
        const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.aliyayman.sozluk4"
    }


}

