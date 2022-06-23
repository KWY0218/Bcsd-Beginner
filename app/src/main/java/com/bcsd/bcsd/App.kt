package com.bcsd.bcsd

import android.app.Application

class App : Application() {
    val repository by lazy { BoardRepository() }
}