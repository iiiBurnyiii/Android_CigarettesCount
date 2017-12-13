package org.test.ciggacount.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import org.test.ciggacount.R
import org.test.ciggacount.activities.MainActivity
import org.test.ciggacount.logic.Count.Companion.countPlus
import org.test.ciggacount.logic.Count.Companion.countSub
import org.test.ciggacount.logic.Count.Companion.insertCountIfNextDay
import org.test.ciggacount.utils.MySharedPreferences

class CigaretteWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val sp = MySharedPreferences(context)

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
                val sp = MySharedPreferences(context)

                insertCountIfNextDay(context)
                val count = when (intent.action) {
                    ACTION_PLUS -> countPlus(sp.getPref("count").toInt())
                    ACTION_SUB -> countSub(sp.getPref("count").toInt())
                    else -> sp.getPref("count").toInt()
                }
                sp.setPref("count", count)

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
                                     appWidgetId: Int, sp: MySharedPreferences) {

            val widgetText: String = sp.getPref("count")

            val views = RemoteViews(context.packageName, R.layout.cigarette_widget)
            views.setTextViewText(R.id.appwidget_text, widgetText)

            val activityIntent = Intent(context, MainActivity::class.java)
            var pIntent = PendingIntent.getActivity(context, appWidgetId, activityIntent, 0)
            views.setOnClickPendingIntent(R.id.appwidget_text, pIntent)

            val plusIntent = with (Intent(context, CigaretteWidget::class.java)) {
                action = ACTION_PLUS
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            pIntent = PendingIntent.getBroadcast(context, appWidgetId, plusIntent, 0)
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