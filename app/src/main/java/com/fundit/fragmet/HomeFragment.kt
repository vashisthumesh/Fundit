package com.fundit.fragmet


import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fundit.R

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private var mTextMessage: TextView? = null
    internal var fragment: Fragment? = null
    internal var fm: FragmentManager? = null


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
