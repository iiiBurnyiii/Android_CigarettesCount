package org.test.ciggacount.activities

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_history.*
import org.jetbrains.anko.db.*
import org.jetbrains.anko.toast
import org.test.ciggacount.R
import org.test.ciggacount.adapters.SectionsPagerAdapter
import org.test.ciggacount.utils.database

class HistoryActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.actDeleteAll -> {
                toast("Не работает")
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun getDataFromProvider(table: String): List<Map<String, Any?>> {
        return database.use {
            select(table).orderBy("_id", SqlOrderDirection.DESC).exec {
                parseList(object : MapRowParser<Map<String, Any?>> {
                            override fun parseRow(columns: Map<String, Any?>): Map<String, Any?> =
                                    columns
                        }
                )
            }
        }
    }

    fun deleteAllItems(table: String) {
        database.use {
            delete(table)
        }
    }

    fun deleteItemWithId(id: Long, table: String) {
        database.use {
            delete(table,
                    "_id = $id")
        }
    }

    fun updateCountWithId(id: Long, count: String) {
        database.use {
            update("Counter", "count" to count.toInt())
                    .whereArgs("_id = {itemId}", "itemId" to id)
                    .exec()
        }
    }

    fun updatePackWithId(id: Long, name: String, price: String) {
        database.use {
            update("CigarettesPacks", "name" to name, "price" to price.toInt())
                    .whereArgs("_id = {itemId}", "itemId" to id)
                    .exec()
        }
    }
}
