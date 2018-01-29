package com.fundit.fragmet


import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fundit.CampaignSetting

import com.fundit.R
import com.fundit.SignInActivity
import com.fundit.a.AppPreference
import com.fundit.a.C
import com.fundit.a.W
import com.fundit.apis.ServiceHandler
import com.fundit.fundspot.AddProductActivity
import com.fundit.fundspot.FundspotCampaignActivity
import com.fundit.model.Fundspot
import com.fundit.model.Member
import com.fundit.model.Organization
import com.fundit.model.User
import com.fundit.organization.CreateCampaignActivity
import com.google.gson.Gson
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class MyCampaignsFragment : Fragment() {

    var img_createCampaign: ImageView? = null
    var preference: AppPreference? = null


    internal var member = Fundspot()
    internal var PRODUCT_REQUEST = 136

    var cartCount: TextView? = null
    var memberRequest: Int = 0
    var campaignRequestCount: Int = 0
    var totalRequest: Int = 0


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
        if (!preference?.userRoleID.equals(C.GENERAL_MEMBER)) {
            val fragment: Fragment = UpcomingCampaignFragment()
            val fm: FragmentManager = activity.supportFragmentManager
            val transaction: FragmentTransaction = fm.beginTransaction()
            transaction.replace(R.id.frame_container, fragment)
            transaction.commit()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        preference = AppPreference(activity)
        Log.e("member", "-->" + preference?.getMemberData())
        try {
            member = Gson().fromJson(preference?.getMemberData(), Fundspot::class.java)
            Log.e("member", "-->" + preference?.getMemberData())

        } catch (e: Exception) {
            Log.e("Exception", e.message)
        }


        val view = inflater!!.inflate(R.layout.fragment_my_campaigns, container, false)
        val navigation = view.findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)



        img_createCampaign = view.findViewById(R.id.img_createCampaign) as ImageView

        if (preference?.userRoleID.equals(C?.GENERAL_MEMBER)) {
            img_createCampaign?.visibility = View.GONE
        }

        img_createCampaign?.setOnClickListener {

            if (preference?.userRoleID.equals(C?.ORGANIZATION)) {
                val intent = Intent(context, CreateCampaignActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            } else if (preference?.userRoleID.equals(C?.FUNDSPOT)) {

                Log.e("settings", "-->" + member.getFundspot_percent() + "==>" + member.getOrganization_percent())

                if (member.getFundspot_percent().equals("0") && member.getOrganization_percent().equals("0")) {
                    OpenPopUpDialog()
                } else if (preference?.getfundspot_product_count() == 0) {

                    OpenProductDialog()

                } else {

                    val intent = Intent(context, FundspotCampaignActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)

                }


            }
        }




        setPresentCampaignFragment()
        navigation.getMenu().getItem(1).setChecked(true);

        //setPastCampaignFragment()

        GetNotificationCount().execute()

        return view
    }

    private fun OpenPopUpDialog() {

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("First set your campaign terms!")
        builder.setMessage("Before starting your first campaign, make sure to set your campaign terms and products.")
        builder.setCancelable(false)
        builder.setPositiveButton("Set Terms") { dialogInterface, i ->


            val intent = Intent(activity, CampaignSetting::class.java)
            intent.putExtra("editMode", true)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)


        }
        builder.setNegativeButton("Cancel") { dialogInterface, i ->

            dialogInterface.dismiss()

        }
        val bDialog = builder.create()
        bDialog.show()


    }


    private fun OpenProductDialog() {

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("First add your Products!")
        builder.setMessage("Please add your products or services available for sale during campaigns.")
        builder.setCancelable(false)
        builder.setPositiveButton("Add Products") { dialogInterface, i ->

            val intent = Intent(context, AddProductActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)


        }
        builder.setNegativeButton("Cancel") { dialogInterface, i ->

            dialogInterface.dismiss()

        }
        val bDialog = builder.create()
        bDialog.show()


    }


    inner class GetNotificationCount : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void): String {


            val parameters = ArrayList<NameValuePair>()

            parameters.add(BasicNameValuePair("user_id", preference?.userID))
            parameters.add(BasicNameValuePair("tokenhash", preference?.tokenHash))


            val json = ServiceHandler().makeServiceCall(W.BASE_URL + W.GetNotificationCount, ServiceHandler.POST, parameters)



            return json
        }


        override fun onPostExecute(s: String) {
            super.onPostExecute(s)


            if (s.equals("", ignoreCase = true) || s.isEmpty()) {


                C.showToast(activity, "Please check your Internet")


            } else {

                try {
                    val mainObject = JSONObject(s)

                    var status = false
                    var message = ""

                    status = mainObject.getBoolean("status")
                    message = mainObject.getString("message")

                    cartCount?.text = mainObject.getString("total_unread_msg")
                    preference?.messageCount = mainObject.getInt("total_unread_msg")

                    memberRequest = mainObject.getInt("total_member_request_count")
                    campaignRequestCount = mainObject.getInt("total_request_count")
                    preference?.setfundspot_product_count(mainObject.getInt("fundspot_product_count"))


                    totalRequest = memberRequest + campaignRequestCount
                    Log.e("totalCount", "--->" + totalRequest)
                    Log.e("totalproductCount", "--->" + preference?.getfundspot_product_count())

                    if (cartCount?.text.toString().equals("0")) {
                        cartCount?.visibility = View.GONE
                    } else {
                        cartCount?.visibility = View.VISIBLE
                    }

                    preference?.campaignCount = campaignRequestCount
                    preference?.memberCount = memberRequest
                    preference?.totalCount = totalRequest

                    Log.e("count", "" + preference?.messageCount)


                } catch (e: JSONException) {
                    e.printStackTrace()
                }


            }


        }
    }


    override fun onResume() {
        super.onResume()
        GetNotificationCount().execute()
    }

}// Required empty public constructor
