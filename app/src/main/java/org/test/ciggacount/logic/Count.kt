package org.test.ciggacount.logic

import android.content.Context
import org.test.ciggacount.utils.DatabaseManager
import org.test.ciggacount.utils.MySharedPreferences
import java.text.DateFormat
import java.util.*

class Count {

    companion object {
        fun countSub(count: Int): Int {
            return if (count <= 0) 0 else {
                var cnt = count
                --cnt
            }
        }

        fun countPlus(count: Int): Int {
            return count + 1
        }

        fun insertCountIfNextDay(ctx: Context) {
            val curDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            val sp = MySharedPreferences(ctx)

            if (curDay.toString() != sp.getPref("day")) {
                val date = DateFormat
                        .getDateInstance(DateFormat.LONG)
                        .format(Date())

                DatabaseManager(ctx).databaseInsertCount(sp.getPref("count"), sp.getPref("date"))

                sp.setPref("count", 0)
                sp.setPref("day", curDay)
                sp.setPref("date", date)
            }
        }
    }
}