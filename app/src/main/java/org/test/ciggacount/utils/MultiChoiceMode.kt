package org.test.ciggacount.utils

//class MultiChoiceMode : AbsListView.MultiChoiceModeListener {
//
//    val itemsList: ArraySet<Long> = ArraySet()
//
//    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
//        mode?.menuInflater?.inflate(R.menu.context_menu_history, menu)
//        return true
//    }
//
//    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean =
//            when (item?.itemId) {
//                R.id.cm_edit -> {
//                    when (table) {
//                        "Counter" -> setCountEditDialog()
//                        "CigarettesPacks" -> setPackEditDialog()
//                    }
//                    mode?.finish()
//                    true
//                }
//                R.id.cm_delete -> {
//                    deleteSelectedItems()
//                    mode?.finish()
//                    true
//                }
//                R.id.cm_select_all -> {
//                    selectAll()
//                    true
//                }
//                else -> false
//            }
//
//    override fun onItemCheckedStateChanged(mode: ActionMode?, position: Int, id: Long, checked: Boolean) {
//        Log.d("myLogs", "position - $position id - $id")
//
//        mode?.title = lv.checkedItemCount.toString()
//
//        when (checked) {
//            true -> {
//                itemsList.add(id)
//                mode?.invalidate()
//                Log.d("myLogs", "Элемент из $table с id: $id был добавлен в список, список: $itemsList")
//            }
//            false -> {
//                itemsList.remove(id)
//                mode?.invalidate()
//                Log.d("myLogs", "Элемент из $table с id: $id был удален из списка, список: $itemsList")
//            }
//        }
//    }
//
//    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
//        menu?.findItem(R.id.cm_edit)?.isVisible = lv.checkedItemCount <= 1
//        return true
//    }
//
//    override fun onDestroyActionMode(mode: ActionMode?) {
//    }
//
//    private fun selectAll() {
//        with(lv) {
//            (0 until childCount)
//                    .forEach { setItemChecked(it,true) }
//        }
//    }
//
//    private fun deleteSelectedItems() {
//        for (item in itemsList) {
//            adapter.deleteItem(item)
//            Log.d("myLogs", "Элемент $item был удалён")
//        }
//        itemsList.clear()
//    }
//
//    private fun updateCount(count: String) {
//        for (item in itemsList) {
//            adapter.updateCount(item, count)
//        }
//        itemsList.clear()
//    }
//
//    private fun updatePack(name: String, price: String) {
//        for (item in itemsList) {
//            adapter.updatePack(item, name, price)
//        }
//        itemsList.clear()
//    }
//
//    private fun setCountEditDialog() {
//        var etEditCount: EditText? = null
//
//        alert {
//            title = "Редактировать количество выкуренных сигарет"
//
//            customView {
//                verticalLayout {
//                    linearLayout {
//                        etEditCount = editText {
//                            hint = "Сколько выкурил"
//
//                            inputType = android.text.InputType.TYPE_CLASS_NUMBER
//                        }.lparams(width = matchParent, height = wrapContent) {
//                            bottom = dip(15)
//                        }
//                    }.lparams(width = matchParent, height = wrapContent)
//                }
//            }
//
//            yesButton { updateCount(etEditCount?.text.toString()) }
//            noButton { itemsList.clear() }
//        }.show()
//    }
//
//    private fun setPackEditDialog() {
//        var etEditName: EditText? = null
//        var etEditPrice: EditText? = null
//
//        alert {
//            title = "Редактировать купленную пачку"
//
//            customView {
//                verticalLayout {
//                    linearLayout {
//                        etEditName = editText {
//                            hint = "Марка"
//
//                            inputType = android.text.InputType.TYPE_CLASS_TEXT
//                        }.lparams(width = matchParent, height = wrapContent) {
//                            bottom = dip(15)
//                        }
//                    }.lparams(width = matchParent, height = wrapContent)
//
//                    linearLayout {
//                        etEditPrice = editText {
//                            hint = "Цена"
//
//                            inputType = android.text.InputType.TYPE_CLASS_NUMBER
//                        }.lparams(width = matchParent, height = wrapContent) {
//                            bottom = dip(15)
//                        }
//                    }.lparams(width = matchParent, height = wrapContent)
//                }
//            }
//            yesButton { updatePack(etEditName?.text.toString(),
//                    etEditPrice?.text.toString()) }
//            noButton { itemsList.clear() }
//        }.show()
//    }
//}
//}