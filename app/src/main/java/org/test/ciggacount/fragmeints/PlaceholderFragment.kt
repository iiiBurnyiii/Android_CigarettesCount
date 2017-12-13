package org.test.ciggacount.fragmeints

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.util.ArraySet
import android.util.Log
import android.view.*
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ListView
import kotlinx.android.synthetic.main.fragment_history.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.alert
import org.test.ciggacount.R
import org.test.ciggacount.activities.HistoryActivity
import org.test.ciggacount.adapters.HistoryAdapter

class PlaceholderFragment : Fragment() {

    lateinit var packName: EditText
    lateinit var packPrice: EditText
    lateinit var count: EditText

    companion object {
        private val ARG_SECTION_ROLE = "section_role"

        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            val fragment = PlaceholderFragment()
            val args = Bundle()

            val sectionRole = when(sectionNumber) {
                0 -> "Counter"
                1 -> "CigarettesPacks"
                else -> "Kek"
            }

            args.putString(ARG_SECTION_ROLE, sectionRole)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_history, container, false)
        val lv = rootView.lvHistory
        val table = arguments.getString(ARG_SECTION_ROLE)
        val adapter = HistoryAdapter((this.context as HistoryActivity), table)

        lv.adapter = adapter

        lv.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
        lv.setMultiChoiceModeListener( object : AbsListView.MultiChoiceModeListener {

            val selectedItemsList: ArraySet<Long> = ArraySet()

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.menuInflater?.inflate(R.menu.context_menu_history, menu)
                return true
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean =
                    when (item?.itemId) {
                        R.id.cm_edit -> {
                            when (table) {
                                "Counter" -> setCountEditDialog()
                                "CigarettesPacks" -> setPackEditDialog()
                            }
                            mode?.finish()
                            true
                        }
                        R.id.cm_delete -> {
                            deleteSelectedItems()
                            mode?.finish()
                            true
                        }
                        R.id.cm_select_all -> {
                            selectAll()
                            true
                        }
                        else -> false
                    }

            override fun onItemCheckedStateChanged(mode: ActionMode?, position: Int, id: Long, checked: Boolean) {
                Log.d("myLogs", "position - $position id - $id")

                mode?.title = lv.checkedItemCount.toString()

                when (checked) {
                    true -> {
                        selectedItemsList.add(id)
                        mode?.invalidate()
                    }
                    false -> {
                        selectedItemsList.remove(id)
                        mode?.invalidate()
                    }
                }
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menu?.findItem(R.id.cm_edit)?.isVisible = lv.checkedItemCount <= 1
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
            }

            private fun selectAll() {
                with(lv) {
                    (0 until childCount)
                            .forEach { setItemChecked(it,true) }
                }
            }

            private fun deleteSelectedItems() {
                for (item in selectedItemsList) {
                    adapter.deleteItem(item)
                    Log.d("myLogs", "Элемент $item был удалён")
                }
                selectedItemsList.clear()
            }

            private fun updateCount(count: String) {
                for (item in selectedItemsList) {
                    adapter.updateCount(item, count)
                }
                selectedItemsList.clear()
            }

            private fun updatePack(name: String, price: String) {
                for (item in selectedItemsList) {
                    adapter.updatePack(item, name, price)
                }
                selectedItemsList.clear()
            }

            private fun setCountEditDialog() {
                alert {
                    titleResource = R.string.edit_count

                    customView {
                        verticalLayout {
                            padding = dip(24)

                            count = editText {
                                hintResource = R.string.hint_count
                                inputType = android.text.InputType.TYPE_CLASS_NUMBER
                            }
                        }
                    }

                    yesButton { updateCount(
                            count = count.text.toString()
                    ) }
                    noButton { selectedItemsList.clear() }
                }.show()
            }

            private fun setPackEditDialog() {
                alert {
                    titleResource = R.string.edit_pack

                    customView {
                        verticalLayout {
                            padding = dip(24)

                            packName = editText {
                                hintResource = R.string.pack_name
                                inputType = android.text.InputType.TYPE_CLASS_TEXT
                            }

                            packPrice = editText {
                                hintResource = R.string.pack_price
                                inputType = android.text.InputType.TYPE_CLASS_NUMBER
                            }
                        }
                    }

                    yesButton { updatePack(
                            name = packName.text.toString(),
                            price = packPrice.text.toString()
                    ) }
                    noButton { selectedItemsList.clear() }
                }.show()
            }
        })

        return rootView
    }

}