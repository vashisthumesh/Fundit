package com.fundit.fragmet


import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.fundit.R


/**
 * A simple [Fragment] subclass.
 */
class MyCampaignsFragment : Fragment() {



    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_past -> {
                setPastCampaignFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_present -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_upcoming -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun setPastCampaignFragment() {
        val fragment: Fragment = PastCampaignFragment()
        val fm: FragmentManager = activity.supportFragmentManager
        val transaction: FragmentTransaction = fm.beginTransaction()
        transaction.replace(R.id.frame_container,fragment)
        transaction.commit()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_my_campaigns, container, false)
        val navigation = view.findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        return view
    }

}// Required empty public constructor
