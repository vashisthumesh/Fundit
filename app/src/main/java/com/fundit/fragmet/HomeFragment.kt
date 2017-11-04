package com.fundit.fragmet


import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.MenuItemCompat
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fundit.R
import com.fundit.a.AppPreference
import com.fundit.a.C

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private var mTextMessage: TextView? = null
    internal var fragment: Fragment? = null
    internal var fm: FragmentManager? = null
    var preferences: AppPreference? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_newsFeed -> {
                setNewsFeedInboxFragment()
                //mTextMessage!!.text = item.title
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_inbox -> {
                setNavigationInboxFragment()
                mTextMessage!!.text = item.title
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_myCampaigns -> {
                setMyCampaignFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_browse -> {
                setBrowseFragment()
                mTextMessage!!.text = item.title
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    private fun setBrowseFragment() {
        fragment = MyBrowseCampaign()
        val transaction: FragmentTransaction = fm!!.beginTransaction()
        transaction.replace(R.id.content_home, fragment)
        transaction.commit()
    }

    private fun setNewsFeedInboxFragment() {
        fragment = NewsFeedFragment()
        val transaction: FragmentTransaction = fm!!.beginTransaction()
        transaction.replace(R.id.content_home, fragment)
        transaction.commit()
    }

    private fun setNavigationInboxFragment() {
        fragment = InboxFragment()
        val transaction: FragmentTransaction = fm!!.beginTransaction()
        transaction.replace(R.id.content_home, fragment)
        transaction.commit()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_home, container, false)

        preferences = AppPreference(activity)

        fm = activity.supportFragmentManager
        mTextMessage = view.findViewById(R.id.message) as TextView

        val navigation = view.findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        if (preferences?.userRoleID.equals(C.FUNDSPOT) && preferences?.isSkiped == true) {

            setMyProductFragment()

        }
        else if(preferences?.userRoleID.equals(C.FUNDSPOT) || preferences?.userRoleID.equals(C.ORGANIZATION) && preferences?.isSkiped == false)
        {
            setMyCampaignFragment()
            navigation.getMenu().getItem(2).setChecked(true);
        }
        else
        {
            setNewsFeedInboxFragment()
            navigation.getMenu().getItem(0).setChecked(true);
        }
        // Inflate the layout for this fragment
        return view
    }

    private fun setMyCampaignFragment() {


            fragment = MyCampaignsFragment()
            val transaction: FragmentTransaction = fm!!.beginTransaction()
            transaction.replace(R.id.content_home, fragment)
            transaction.commit()

    }

    private fun setMyProductFragment() {

        fragment = MyProductsFragment()
        val transaction: FragmentTransaction = fm!!.beginTransaction()
        transaction.replace(R.id.content_home, fragment)
        transaction.commit()
    }

}
