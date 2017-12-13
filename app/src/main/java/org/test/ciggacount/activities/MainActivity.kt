package org.test.ciggacount.activities

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.*
import org.test.ciggacount.R
import org.test.ciggacount.logic.Count.Companion.countPlus
import org.test.ciggacount.logic.Count.Companion.countSub
import org.test.ciggacount.logic.Count.Companion.insertCountIfNextDay
import org.test.ciggacount.utils.DatabaseManager
import org.test.ciggacount.utils.MySharedPreferences
import org.test.ciggacount.widget.CigaretteWidget

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var  name: EditText
    private lateinit var price: EditText

    private val sp = MySharedPreferences(this)
    private val dbManager = DatabaseManager(this)

    private val onClick = View.OnClickListener {view ->
        insertCountIfNextDay(this)

        when (view.id) {
            R.id.btnPlus -> sp.setPref("count", countPlus(sp.getPref("count").toInt()))

            R.id.btnSub -> sp.setPref("count", countSub(sp.getPref("count").toInt()))
        }

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

        fab_pack.setOnClickListener {
            alert {
                titleResource = R.string.add_pack

                customView {
                    verticalLayout {
                        padding = dip(24)

                        name = editText {
                            hintResource = R.string.pack_name
                            inputType = android.text.InputType.TYPE_CLASS_TEXT
                        }

                        price = editText {
                            hintResource = R.string.pack_price
                            inputType = android.text.InputType.TYPE_CLASS_NUMBER
                        }
                    }
                }

                yesButton { dbManager.databaseInsertPack(
                        name = name.text.toString(),
                        price = price.text.toString()
                ) }
                noButton {  }
            }.show()
        }
    }

    override fun onResume() {
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
            R.id.nav_add_count -> dbManager.dbInsertTestCount()
            R.id.nav_add_pack -> dbManager.dbInsertTestPack()
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
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
}