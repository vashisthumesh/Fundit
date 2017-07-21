package com.fundit.fragmet


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.fundit.R
import com.fundit.organization.CreateCampaignActivity


/**
 * A simple [Fragment] subclass.
 */
class MyCampaignsFragment : Fragment() {

    var img_createCampaign: ImageView? = null

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

        img_createCampaign = view.findViewById(R.id.img_createCampaign) as ImageView

        img_createCampaign?.setOnClickListener {
            val intent = Intent(context, CreateCampaignActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        return view
    }

}// Required empty public constructor
