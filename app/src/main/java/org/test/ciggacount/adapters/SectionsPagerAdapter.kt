package org.test.ciggacount.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import org.test.ciggacount.fragmeints.PlaceholderFragment


class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = PlaceholderFragment.newInstance(position)

    override fun getCount(): Int = 2
}
