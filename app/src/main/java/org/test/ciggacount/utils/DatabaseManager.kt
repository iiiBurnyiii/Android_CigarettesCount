package org.test.ciggacount.utils

import android.content.Context
import org.jetbrains.anko.db.*
import java.text.DateFormat
import java.util.*


val Context.database: DatabaseHelper
    get() = DatabaseHelper.getInstance(applicationContext)


class DatabaseManager(private val ctx: Context) {

    fun getDataFromProvider(table: String): List<Map<String, Any?>> {
        return ctx.database.use {
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
        ctx.database.use {
            delete(table)
        }
    }

    fun databaseInsertPack(name: String, price: String) {
        val date = DateFormat
                .getDateInstance(DateFormat.LONG)
                .format(Date())

        ctx.database.use {
            insert("CigarettesPacks",
                    "date" to date,
                    "name" to name,
                    "price" to price.toInt())
        }
    }

    fun databaseInsertCount(count: String, date: String) {
        ctx.database.use {
            insert("Counter",
                    "count" to count.toInt(),
                    "date" to date)
        }
    }



    fun deleteItemWithId(id: Long, table: String) {
        ctx.database.use {
            delete(table,
                    "_id = $id")
        }
    }

    fun updateCountWithId(id: Long, count: String) {
        ctx.database.use {
            update("Counter", "count" to count.toInt())
                    .whereArgs("_id = {itemId}", "itemId" to id)
                    .exec()
        }
    }

    fun updatePackWithId(id: Long, name: String, price: String) {
        ctx.database.use {
            update("CigarettesPacks", "name" to name, "price" to price.toInt())
                    .whereArgs("_id = {itemId}", "itemId" to id)
                    .exec()
        }
    }


    fun dbInsertTestCount() {
        ctx.database.use {
            for (i in 1 until 10) {
                insert("Counter",
                        "count" to i,
                        "date" to "TEST")
            }
        }
    }

    fun dbInsertTestPack() {
        ctx.database.use {
            for (i in 1 until 10) {
                insert("CigarettesPacks",
                        "date" to "TEST",
                        "name" to "TEST",
                        "price" to "1000".toInt())
            }
        }
    }
}