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
import com.fundit.a.AppPreference
import com.fundit.a.C
import com.fundit.fundspot.FundspotCampaignActivity
import com.fundit.organization.CreateCampaignActivity


/**
 * A simple [Fragment] subclass.
 */
class MyCampaignsFragment : Fragment() {

    var img_createCampaign: ImageView? = null
    var preference: AppPreference? = null




    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_past -> {
                setPastCampaignFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_present -> {
                setPresentCampaignFragment()
                return@OnNavigationItemSelectedListener true
            }
//            R.id.menu_upcoming -> {
//                setUpcomingCampaignFragment()
//                return@OnNavigationItemSelectedListener true
//            }
        }
        false
    }

    private fun setPastCampaignFragment() {
        //if(!preference?.userRoleID.equals(C.GENERAL_MEMBER)) {
            val fragment: Fragment = PastCampaignFragment()
            val fm: FragmentManager = activity.supportFragmentManager
            val transaction: FragmentTransaction = fm.beginTransaction()
            transaction.replace(R.id.frame_container, fragment)
            transaction.commit()
        //}

    }

    private fun setPresentCampaignFragment() {
      //  if(!preference?.userRoleID.equals(C.GENERAL_MEMBER)) {
            val fragment: Fragment = PresentCampaignFragment()
            val fm: FragmentManager = activity.supportFragmentManager
            val transaction: FragmentTransaction = fm.beginTransaction()
            transaction.replace(R.id.frame_container, fragment)
            transaction.commit()
       // }
    }

    private fun setUpcomingCampaignFragment() {
        if(!preference?.userRoleID.equals(C.GENERAL_MEMBER)) {
            val fragment: Fragment = UpcomingCampaignFragment()
            val fm: FragmentManager = activity.supportFragmentManager
            val transaction: FragmentTransaction = fm.beginTransaction()
            transaction.replace(R.id.frame_container, fragment)
            transaction.commit()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_my_campaigns, container, false)
        val navigation = view.findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        preference = AppPreference(activity)
        img_createCampaign = view.findViewById(R.id.img_createCampaign) as ImageView

        if(preference?.userRoleID.equals(C?.GENERAL_MEMBER)){
            img_createCampaign?.visibility = View.GONE
        }

        img_createCampaign?.setOnClickListener {

            if(preference?.userRoleID.equals(C?.ORGANIZATION)) {
                val intent = Intent(context, CreateCampaignActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }else if(preference?.userRoleID.equals(C?.FUNDSPOT)){
                val intent = Intent(context, FundspotCampaignActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)

            }
        }



                setPresentCampaignFragment()
        navigation.getMenu().getItem(1).setChecked(true);
            //setPastCampaignFragment()


        return view
    }

}// Required empty public constructor
