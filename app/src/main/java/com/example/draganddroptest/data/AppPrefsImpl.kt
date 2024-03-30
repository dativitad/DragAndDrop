package com.example.draganddroptest.data

import android.content.Context
import android.content.SharedPreferences

class AppPrefsImpl(private val appContext: Context) : AppPrefs {

    private val sharedPrefs: SharedPreferences =
        appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveXY(x: Float, y: Float) {
        sharedPrefs.edit()
            .putFloat(PREFS_X_KEY, x)
            .putFloat(PREFS_Y_KEY, y)
            .apply()
    }

    override fun getX(): Float = sharedPrefs.getFloat(PREFS_X_KEY, 0f)

    override fun getY(): Float = sharedPrefs.getFloat(PREFS_Y_KEY, 0f)

    companion object {
        private const val PREFS_NAME = "xy_prefs"
        private const val PREFS_X_KEY = "x"
        private const val PREFS_Y_KEY = "y"
    }
}