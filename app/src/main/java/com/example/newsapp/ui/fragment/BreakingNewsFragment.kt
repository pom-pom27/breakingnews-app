package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import kotlin.system.exitProcess

class BreakingNewsFragment : Fragment(R.layout.breaking_news_fragment) {

    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (backPressedTime + 3000 > System.currentTimeMillis()) {
                Log.d("TAG", "onCreate: press one more to exit")

                exitProcess(1)
            } else {
                Log.d("TAG", "onCreate: press 1 more to exit")
            }
            backPressedTime = System.currentTimeMillis()
        }
    }
}
