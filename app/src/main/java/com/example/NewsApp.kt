package com.example

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsApp : Application()

//TODO: convert to flow
//TODO: bug: can saved identical article in saved database
