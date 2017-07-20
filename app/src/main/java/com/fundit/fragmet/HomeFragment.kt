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
import android.widget.TextView

import com.fundit.R
import com.fundit.organization.CreateCampaignActivity

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private var mTextMessage: TextView? = null
    internal var fragment: Fragment? = null
    internal var fm: FragmentManager? = null
    var img_createCampaign: ImageView? = null


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_newsFeed -> {
                mTextMessage!!.text = item.title
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_inbox -> {
                mTextMessage!!.text = item.title
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_myCampaigns -> {
                setMyCampaignFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_browse -> {
                mTextMessage!!.text = item.title
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view=inflater!!.inflate(R.layout.fragment_home, container, false)

        fm = activity.supportFragmentManager

        img_createCampaign = view.findViewById(R.id.img_createCampaign) as ImageView

        img_createCampaign?.setOnClickListener {
            val intent = Intent(context, CreateCampaignActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        mTextMessage = view.findViewById(R.id.message) as TextView
        val navigation = view.findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        // Inflate the layout for this fragment
        return view
    }

    private fun setMyCampaignFragment() {
        fragment = MyCampaignsFragment()
        val transaction: FragmentTransaction = fm!!.beginTransaction()
        transaction.replace(R.id.content_home, fragment)
        transaction.commit()
    }

}
