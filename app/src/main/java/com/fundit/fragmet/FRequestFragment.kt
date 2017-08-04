package com.fundit.fragmet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fundit.R

/**
 * Created by Nivida new on 25-Jul-17.
 */
class FRequestFragment : Fragment() {

    var fView: View? = null
    var requestCampaignFragment: FundspotRequestFragment? = null
    var requestMemberFragment: Fragment? = null
    internal var SUCCESS_CODE = 475

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_requestCampaign -> {
                showCampaignFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_requestMember -> {
                showMemberFragment()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun showMemberFragment() {
        requestMemberFragment = MemberRequestFragment()
        val fm: FragmentManager = activity.supportFragmentManager
        val transaction: FragmentTransaction = fm.beginTransaction()
        transaction.replace(R.id.frame_request, requestMemberFragment)
        transaction.commit()
    }

    private fun showCampaignFragment() {
        requestCampaignFragment = FundspotRequestFragment()
        val fm: FragmentManager = activity.supportFragmentManager
        val transaction: FragmentTransaction = fm.beginTransaction()
        transaction.replace(R.id.frame_request, requestCampaignFragment)
        transaction.commit()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fView = inflater?.inflate(R.layout.fragment_reuest_home, container, false)

        fetchIDs()

        return fView
    }

    private fun fetchIDs() {
        requestCampaignFragment = FundspotRequestFragment()
        requestMemberFragment = Fragment()
        val navigation = fView?.findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        showCampaignFragment()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Log.e("Activity","Result"+ requestCode +" - "+ resultCode);

        if(requestCode==SUCCESS_CODE && resultCode== Activity.RESULT_OK){
            showCampaignFragment()
        }
    }
}