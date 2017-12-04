package org.test.ciggacount.utils

import android.content.Context

class MySharedPreferences(private val context: Context) {

    companion object {

        val WIDGET_PREF = "widget_pref"
    }

    fun setPref(key: String, value: Any?) {
        context.getSharedPreferences(MySharedPreferences.WIDGET_PREF, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value.toString())
                .apply()

    }

    fun getPref(key: String): String {
        return context.getSharedPreferences(MySharedPreferences.WIDGET_PREF, Context.MODE_PRIVATE)
                .getString(key, "0")
    }
}