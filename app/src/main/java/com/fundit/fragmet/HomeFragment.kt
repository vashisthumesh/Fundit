package com.fundit.fragmet


import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.MenuItemCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fundit.R
import com.fundit.a.AppPreference
import com.fundit.a.C
import com.fundit.a.W
import com.fundit.apis.ServiceHandler
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private var mTextMessage: TextView? = null
    internal var fragment: Fragment? = null
    internal var fm: FragmentManager? = null
    var preferences: AppPreference? = null

    var cartCount: TextView? = null
    var memberRequest: Int = 0
    var campaignRequestCount: Int = 0
    var couponCount: Int = 0
    var totalRequest: Int = 0


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
        fragment = NewsFragment()
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


        /*if (preferences?.userRoleID.equals(C.FUNDSPOT) && preferences?.isSkiped == true) {

            setMyProductFragment()

        }
        else*/ if(preferences?.userRoleID.equals(C.FUNDSPOT) || preferences?.userRoleID.equals(C.ORGANIZATION) && preferences?.isSkiped == false)
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

        GetNotificationCount().execute()

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

    inner class GetNotificationCount : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void): String {


            val parameters = ArrayList<NameValuePair>()

            parameters.add(BasicNameValuePair("user_id", preferences?.userID))
            parameters.add(BasicNameValuePair("tokenhash", preferences?.tokenHash))


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
                    preferences?.messageCount = mainObject.getInt("total_unread_msg")

                    memberRequest = mainObject.getInt("total_member_request_count")
                    campaignRequestCount = mainObject.getInt("total_request_count")
                    couponCount = mainObject.getInt("total_coupon_count")
                    preferences?.setfundspot_product_count(mainObject.getInt("fundspot_product_count"))
                    preferences?.setRedeemer(mainObject.getInt("is_redeemer"))
                    preferences?.setSeller(mainObject.getInt("is_seller"))


                    totalRequest = memberRequest + campaignRequestCount
                    Log.e("totalCount", "--->" + totalRequest)
                    Log.e("totalproductCount", "--->" + preferences?.getfundspot_product_count())


                    preferences?.campaignCount = campaignRequestCount
                    preferences?.memberCount = memberRequest
                    preferences?.totalCount = totalRequest
                    preferences?.couponCount = couponCount



                  //  navigationAdapter?.notifyDataSetChanged()



                    Log.e("count", "" + preferences?.messageCount)


                } catch (e: JSONException) {
                    e.printStackTrace()
                }


            }


        }
    }

}
