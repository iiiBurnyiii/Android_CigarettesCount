package org.test.ciggacount.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import org.jetbrains.anko.*
import org.test.ciggacount.R
import org.test.ciggacount.activities.HistoryActivity

class HistoryAdapter(private val activity: HistoryActivity, private val table: String) : BaseAdapter() {

    private var list: List<Map<String, Any?>> = activity.getDataFromProvider(table)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = getItem(position)

        val itemText = when (table) {
            "Counter" -> "За ${item["date"]} ты выкурил ${item["count"]} сигарет"
            "CigarettesPacks" -> "${item["date"]} ты купил ${item["name"]} за ${item["price"]} рублей"
            else -> "Что-то пошло не так"
        }

        return with(parent!!.context) {
            linearLayout {
                backgroundResource = R.drawable.multiple_selected_item

                textView {
                    text = itemText

                    textSize = 16F
                }.lparams(width = matchParent, height = wrapContent) {
                    margin = dip(15)
                }
            }
        }
    }

    override fun getItem(position: Int): Map<String, Any?> = list[position]

    override fun getItemId(position: Int): Long = getItem(position)["_id"] as Long

    override fun getCount(): Int = list.size

    private fun rebuild() {
        list = activity.getDataFromProvider(table)
        notifyDataSetChanged()
    }

    fun deleteItem(id: Long) {
        activity.deleteItemWithId(id, table)
        rebuild()
    }

    fun updateCount(id: Long, count: String) {
        activity.updateCountWithId(id, count)
        rebuild()
    }

    fun updatePack(id: Long, name: String, price: String) {
        activity.updatePackWithId(id, name, price)
        rebuild()
    }


}