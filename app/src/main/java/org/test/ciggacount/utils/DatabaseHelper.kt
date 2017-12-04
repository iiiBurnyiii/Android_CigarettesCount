package org.test.ciggacount.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.jetbrains.anko.db.*

class DatabaseHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MyDB") {

    companion object {
        private var instance: DatabaseHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseHelper {
            if (instance == null) {
                instance = DatabaseHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("myLogs", "db onCreate")

        db.createTable("Counter", false,
                "_id" to INTEGER + PRIMARY_KEY,
                "date" to TEXT,
                "count" to INTEGER)

        db.createTable("CigarettesPacks", false,
                "_id" to INTEGER + PRIMARY_KEY,
                "date" to TEXT,
                "name" to TEXT,
                "price" to INTEGER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}