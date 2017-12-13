package org.test.ciggacount.utils

import android.content.Context

class MySharedPreferences(private val ctx: Context) {

    companion object {
        val APP_PREF = "app_pref"
    }

    fun setPref(key: String, value: Any?) {
        ctx.getSharedPreferences(MySharedPreferences.APP_PREF, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value.toString())
                .apply()

    }

    fun getPref(key: String): String {
        return ctx.getSharedPreferences(MySharedPreferences.APP_PREF, Context.MODE_PRIVATE)
                .getString(key, "0")
    }
}