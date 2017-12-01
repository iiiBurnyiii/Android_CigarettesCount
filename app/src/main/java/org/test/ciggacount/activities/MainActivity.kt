package org.test.ciggacount.activities

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.test.ciggacount.DatabaseHelper
import org.test.ciggacount.R
import org.test.ciggacount.widget.CigaretteWidget
import java.text.DateFormat
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        val WIDGET_PREF = "widget_pref"

        var count = 0

        fun subCount(count: Int): Int {
            return if (count <= 0) 0 else {
                var cnt = count
                --cnt
            }
        }
    }

    private lateinit var  name: String
    private lateinit var price: String
    private var curDate =  Calendar.getInstance().timeInMillis
    private var curDay: Int? = null

    private val Context.database : DatabaseHelper
        get() = DatabaseHelper.getInstance(applicationContext)

    private val onClick = View.OnClickListener {view ->
        checkDayAndUpdDb()

        when (view.id) {
            R.id.btnPlus -> {
                count += 1
            }
            R.id.btnSub -> {
                count = subCount(count)
            }
        }

        setPref("count", count)
        tvCount.text = getPref("count")

        updateWidgets()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this,
                drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        btnPlus.setOnClickListener(onClick)
        btnSub.setOnClickListener(onClick)

        btnAddPack.setOnClickListener {
            if (TextUtils.isEmpty(etName.text.toString()) ||
                    TextUtils.isEmpty(etPrice.text.toString())) {
                toast("Введите название марки и цену")
            } else {
                name = etName.text.toString()
                price = etPrice.text.toString()

                databaseInsertPack()
            }
        }
    }

    override fun onResume() {
        if (getPref("count") == "") setPref("count", 0)

        count = getPref("count").toInt()
        tvCount.text = getPref("count")

        super.onResume()
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_history -> startActivity<HistoryActivity>()
            R.id.nav_stat -> toast("Падажжи")
            R.id.nav_add_count -> dbInsertTestCount()
            R.id.nav_add_pack -> dbInsertTestPack()
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    fun checkDayAndUpdDb() {
        curDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        if (curDay.toString() != getPref("day")) {
            databaseInsertCount()
        }
    }


    private fun setPref(key: String, value: Any?) {
        getSharedPreferences(WIDGET_PREF, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value.toString())
                .apply()

    }

    private fun getPref(key: String): String {
        return getSharedPreferences(WIDGET_PREF, Context.MODE_PRIVATE)
                .getString(key, "")
    }


    private fun updateWidgets() {
        val widgetManager = AppWidgetManager.getInstance(this)
        val widgetIds = widgetManager.getAppWidgetIds(
                ComponentName(this, CigaretteWidget::class.java))

        val intent = Intent()
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

        this.sendBroadcast(intent)
    }


    private fun databaseInsertPack() {
        val date = DateFormat
                .getDateInstance(DateFormat.LONG)
                .format(curDate)

        database.use {
            insert("CigarettesPacks",
                    "date" to date,
                    "name" to name,
                    "price" to price.toInt())
        }

        etName.text.clear()
        etPrice.text.clear()
    }

    private fun databaseInsertCount() {
        val date = DateFormat
                .getDateInstance(DateFormat.LONG)
                .format(curDate)

        database.use {
            insert("Counter",
                    "count" to getPref("count").toInt(),
                    "date" to date)
        }

        setPref("day", curDay)

        count = 0
        setPref("count", count)
        tvCount.text = getPref("count")
    }


    private fun dbInsertTestCount() {
        database.use {
            for (i in 1 until 10) {
                insert("Counter",
                        "count" to i,
                        "date" to "TEST")
            }
        }
    }

    private fun dbInsertTestPack() {
        database.use {
            for (i in 1 until 10) {
                insert("CigarettesPacks",
                        "date" to "TEST",
                        "name" to "TEST",
                        "price" to "1000".toInt())
            }
        }
    }
}