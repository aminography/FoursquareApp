package com.aminography.foursquareapp.core

import androidx.multidex.MultiDexApplication
import androidx.appcompat.app.AppCompatDelegate
import com.aminography.foursquareapp.core.di.appComponent
import org.koin.android.ext.android.startKoin

/**
 * Application class
 *
 * @author aminography
 */
@Suppress("unused")
class MyApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, appComponent)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}