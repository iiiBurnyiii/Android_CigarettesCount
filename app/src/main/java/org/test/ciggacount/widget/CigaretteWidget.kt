package org.test.ciggacount.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.RemoteViews
import org.test.ciggacount.R
import org.test.ciggacount.activities.MainActivity

class CigaretteWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val sp = context.getSharedPreferences("widget_pref", Context.MODE_PRIVATE)

        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, sp)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == ACTION_PLUS || intent.action == ACTION_SUB) {
            var mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

            val extras = intent.extras
            if (extras != null) {
                mAppWidgetId = extras.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID
                )
            }
            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {

                val sp = context.getSharedPreferences(MainActivity.WIDGET_PREF, Context.MODE_PRIVATE)

                val count = when (intent.action) {
                    ACTION_PLUS -> sp.getString("count", "0").toInt() + 1
                    ACTION_SUB -> MainActivity.subCount(sp.getString("count", "0").toInt())
                    else -> sp.getString("count", "0").toInt()
                }
                sp.edit().putString("count", count.toString()).apply()

                updateAppWidget(context, AppWidgetManager.getInstance(context), mAppWidgetId, sp)
            }
        }
    }

    override fun onEnabled(context: Context) {
    }

    override fun onDisabled(context: Context) {
    }

    companion object {

        private val ACTION_PLUS = "org.test.ciggacount.count_plus"
        private val ACTION_SUB = "org.test.ciggacount.count_sub"

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int, sp: SharedPreferences) {

            val widgetText: String = sp.getString("count", "0")

            val views = RemoteViews(context.packageName, R.layout.cigarette_widget)
            views.setTextViewText(R.id.appwidget_text, widgetText)


            val plusIntent = with (Intent(context, CigaretteWidget::class.java)) {
                action = ACTION_PLUS
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            var pIntent = PendingIntent.getBroadcast(context, appWidgetId, plusIntent, 0)
            views.setOnClickPendingIntent(R.id.wBtnPlus, pIntent)

            val subIntent = with (Intent(context, CigaretteWidget::class.java)) {
                action = ACTION_SUB
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            pIntent = PendingIntent.getBroadcast(context, appWidgetId, subIntent, 0)
            views.setOnClickPendingIntent(R.id.wBtnSub, pIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}