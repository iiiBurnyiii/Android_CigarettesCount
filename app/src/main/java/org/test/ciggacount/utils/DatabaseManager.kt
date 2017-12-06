package org.test.ciggacount.utils

import android.content.Context
import org.jetbrains.anko.db.delete

class DatabaseManager(private val context: Context) {

    private val db = context.database

    fun deleteAllItems(table: String) {
        db.use {
            delete(table)
        }
    }
}