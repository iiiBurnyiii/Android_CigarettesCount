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
import org.test.ciggacount.R
import org.test.ciggacount.utils.MySharedPreferences
import org.test.ciggacount.utils.database
import org.test.ciggacount.widget.CigaretteWidget
import java.text.DateFormat
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {

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

    private val sp = MySharedPreferences(this)

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

        sp.setPref("count", count)
        tvCount.text = sp.getPref("count")

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
        count = sp.getPref("count").toInt()
        tvCount.text = sp.getPref("count")

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


    private fun checkDayAndUpdDb() {
        curDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        if (curDay.toString() != sp.getPref("day")) {
            databaseInsertCount(this)
        }
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

    private fun databaseInsertCount(context: Context) {
        val date = DateFormat
                .getDateInstance(DateFormat.LONG)
                .format(curDate)

        context.database.use {
            insert("Counter",
                    "count" to sp.getPref("count").toInt(),
                    "date" to date)
        }

        sp.setPref("day", curDay)

        count = 0
        sp.setPref("count", count)
        tvCount.text = sp.getPref("count")
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