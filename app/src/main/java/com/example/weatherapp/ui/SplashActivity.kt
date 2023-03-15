package com.example.weatherapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.data.utils.SharedPrefData
import com.example.weatherapp.data.utils.Utility

class SplashActivity : AppCompatActivity() {
    lateinit var lottieAnimationView: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        SharedPrefData.setupSharedPrefrences(this)
        if(SharedPrefData.theme== Utility.dark){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        }
        lottieAnimationView=findViewById(R.id.gif_splash)

        lottieAnimationView.animate().setDuration(1500)
            .alpha(1f).withEndAction {
                val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                finish()
            }


    }
}