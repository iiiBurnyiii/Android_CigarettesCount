package org.test.ciggacount.widget

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import org.test.ciggacount.R

class ConfigActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_config)
    }
}