package com.fundit

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.fundit.a.C
import com.fundit.fragmet.MyCampaignsFragment

class HomeActivity : AppCompatActivity() {

    private var mTextMessage: TextView? = null

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

    private fun setMyCampaignFragment() {
        val fragment:Fragment = MyCampaignsFragment()
        val fm: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fm.beginTransaction()
        transaction.replace(R.id.content,fragment)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mTextMessage = findViewById(R.id.message) as TextView
        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


    }

}
