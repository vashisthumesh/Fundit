package com.fundit

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.fundit.a.AppPreference
import com.fundit.a.C
import com.fundit.a.W
import com.fundit.apis.Internet
import com.fundit.apis.ServiceHandler
import com.fundit.fragmet.*
import com.fundit.model.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class HomeActivity : AppCompatActivity() {

    internal var list_navigation: ListView? = null
    var headerView: View? = null
    var navigationAdapter: LeftNavigationAdapter? = null
    var menuList: Array<String> = arrayOf()
    var fragment: Fragment? = null
    var fm: FragmentManager? = null
    var toolbar: Toolbar? = null
    var actionTitle: TextView? = null
    var preference: AppPreference? = null
    var drawerLayout: DrawerLayout? = null
    var drawerToggle: ActionBarDrawerToggle? = null
    var isDrawerOpen: Boolean = false
    var txt_userName: TextView? = null
    var txt_userEmail: TextView? = null
    var img_profilePic: CircleImageView? = null
    lateinit var img_edit: ImageView
    lateinit var img_notification: ImageView
    lateinit var img_qrscan: ImageView
    var cartCount: TextView? = null
    var memberRequest: Int = 0
    var campaignRequestCount: Int = 0
    var totalRequest: Int = 0
    var flag=false
    internal var user = User()

    internal var member = Member()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        preference = AppPreference(this)
        var intent:Intent=getIntent()
        flag=intent.getBooleanExtra("flag",false)


        try {
            member = Gson().fromJson(preference?.getMemberData(), Member::class.java)

            Log.e("getMemberData" , "-->" +  member.toString())

        } catch (e: Exception) {
            Log.e("Exception", e.message)
        }



        fillMenus()
        setupToolbar()
        fetchIDs()

    }

    private fun fillMenus() {
        when (preference?.userRoleID) {
            C.ORGANIZATION -> {
                menuList = arrayOf("Home", "My Profile", "Requests", "My Members", "Save Cards", "Settings", "Invite Friends", "Help", "Logout")

            }
            C.FUNDSPOT -> {
                menuList = arrayOf("Home", "My Profile", "Requests", "My Product", "My Members", "Save Cards", "Settings", "Invite Friends", "Help", "Logout")


            }
            C.GENERAL_MEMBER -> {
                menuList = arrayOf("Home", "My Profile", "My Coupons", "My Orders", "Save Cards", "Settings", "Invite Friends", "Help", "Logout")

            }
        }
    }

    private fun setupToolbar() {
        toolbar = C.findToolbarCenteredText(this)
        actionTitle = findViewById(R.id.actionTitle) as TextView
        img_edit = findViewById(R.id.img_edit) as ImageView
        img_notification = findViewById(R.id.img_notification) as ImageView
        img_qrscan = findViewById(R.id.img_qrscan) as ImageView
        cartCount = findViewById(R.id.cartTotal) as TextView
        //cartCount?.text = preference?.messageCount.toString()
        //actionTitle?.text = ""
        setSupportActionBar(toolbar)
        Log.e("redemerId" , "-->" +  member.redeemer.toString())

        if (preference?.userRoleID.equals(C.FUNDSPOT) || (preference?.userRoleID.equals(C.GENERAL_MEMBER)&& member.redeemer.equals("1"))) {
            img_qrscan?.visibility = View.VISIBLE
        } else {
            img_qrscan?.visibility = View.GONE
        }

        img_notification.setOnClickListener {
            val intent = Intent(this@HomeActivity, NotificationActivity::class.java)
            startActivity(intent)
        }

        img_qrscan.setOnClickListener {
            val intent = Intent(this@HomeActivity, QRScannerActivity::class.java)
            startActivity(intent)
        }


        img_edit.setOnClickListener {

            val editMode: Boolean = true
            when (preference?.userRoleID) {
                C.ORGANIZATION -> {

                    val intent = Intent(this@HomeActivity, OrganizationProfileActivity::class.java)
                    intent.putExtra("isEdiMode", editMode)
                    startActivity(intent)


                }
                C.FUNDSPOT -> {


                    val intent = Intent(this@HomeActivity, FundSpotProfile::class.java)
                    intent.putExtra("isEditMode", editMode)
                    startActivity(intent)


                }
                C.GENERAL_MEMBER -> {


                    val intent = Intent(this@HomeActivity, GeneralMemberProfileActivity::class.java)
                    intent.putExtra("isEditMode", editMode)
                    startActivity(intent)


                }


            }


        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    private fun fetchIDs() {

        Log.e("roleID", preference?.userRoleID + " - " + preference?.userID + " - " + preference?.tokenHash)


        when (preference?.userRoleID) {
            C.ORGANIZATION -> fragment = HomeFragment()
            C.FUNDSPOT -> fragment = HomeFragment()
            C.GENERAL_MEMBER -> fragment = HomeFragment()
            else -> fragment = Fragment()
        }

        if (flag == true)
        {
            coupon()

        }
        drawerLayout = findViewById(R.id.drawerLayout) as DrawerLayout
        drawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            override fun onDrawerOpened(drawerView: View?) {
                super.onDrawerOpened(drawerView)
                isDrawerOpen = true
                invalidateOptionsMenu()
            }

            override fun onDrawerClosed(drawerView: View?) {
                super.onDrawerClosed(drawerView)
                isDrawerOpen = false
                invalidateOptionsMenu()
            }
        }

        drawerLayout?.setDrawerListener(drawerToggle)
        drawerToggle?.syncState()


        fm = supportFragmentManager
        val transaction = fm?.beginTransaction()

        transaction?.replace(R.id.content, fragment)
        transaction?.commit()

        list_navigation = findViewById(R.id.list_navigation) as ListView
        headerView = View.inflate(this, R.layout.navigation_drawer, null)
        txt_userName = headerView?.findViewById(R.id.txt_userName) as TextView
        txt_userEmail = headerView?.findViewById(R.id.txt_userEmail) as TextView
        img_profilePic = headerView?.findViewById(R.id.img_profilePic) as CircleImageView
        list_navigation?.addHeaderView(headerView)
        val gson = Gson()
        try{
            user = gson.fromJson(preference?.userData, User::class.java)
        }
        catch(e: JSONException)
        {
            e.printStackTrace();
        }


        txt_userEmail?.text = user.email_id
        when (preference?.userRoleID) {
            C.ORGANIZATION -> {
                val organization: Organization = gson.fromJson(preference?.memberData, Organization::class.java)
                Picasso.with(this)
                        .load(W.FILE_URL + organization.image)
                        .into(img_profilePic)
                txt_userName?.text = organization.title
            }
            C.FUNDSPOT -> {
                val fundspot: Fundspot = gson.fromJson(preference?.memberData, Fundspot::class.java)
                Picasso.with(this)
                        .load(W.FILE_URL + fundspot.image)
                        .into(img_profilePic)
                txt_userName?.text = fundspot.title
            }
            C.GENERAL_MEMBER -> {
                val member: Member = gson.fromJson(preference?.memberData, Member::class.java)
                Picasso.with(this)
                        .load(W.FILE_URL + member.image)
                        .into(img_profilePic)
                txt_userName?.text = member.title
            }
        }

        navigationAdapter = LeftNavigationAdapter(this, menuList, preference)
        list_navigation?.adapter = navigationAdapter
        list_navigation?.setOnItemClickListener { _, _, i, _ -> handleClicks(i) }



        if (Internet.isConnectingToInternet(applicationContext))
            GetNotificationCount().execute()
        else
            Internet.noInternet(applicationContext)
    }

    private fun handleClicks(position: Int) {
        if (position == 0) {

        } else if (position == 1) {
            actionTitle?.text = "Fundit"
            when (preference?.userRoleID) {
                C.ORGANIZATION -> {
                    fragment = HomeFragment()
                }
                C.FUNDSPOT -> {
                    fragment = HomeFragment()
                }
                C.GENERAL_MEMBER -> {
                    fragment = HomeFragment()
                }
            }
            val transaction = fm?.beginTransaction()
            transaction?.replace(R.id.content, fragment)
            transaction?.commit()

        } else if (position == 2) {
            img_edit?.visibility = View.VISIBLE
            actionTitle?.text = "My Profile"
            fragment = MyProfileFragment()
            val transaction = fm?.beginTransaction()
            transaction?.replace(R.id.content, fragment)
            transaction?.commit()
        } else if (position == 3) {
            img_edit?.visibility = View.GONE
            if (preference?.userRoleID.equals(C.FUNDSPOT) || preference?.userRoleID.equals(C.ORGANIZATION)) {
                actionTitle?.text = "Requests"
                fragment = FRequestFragment()
                val transaction = fm?.beginTransaction()
                transaction?.replace(R.id.content, fragment)
                transaction?.commit()
            }
            if (preference?.userRoleID.equals(C.GENERAL_MEMBER)) {
                actionTitle?.text = "My Coupons"
                fragment = CouponFragment()
                val transaction = fm?.beginTransaction()
                transaction?.replace(R.id.content, fragment)
                transaction?.commit()
            }
        } else if (position == 4) {
            img_edit?.visibility = View.GONE
            if (preference?.userRoleID.equals(C.FUNDSPOT)) {
                actionTitle?.text = "My Product"
                fragment = MyProductsFragment()
                val transaction = fm?.beginTransaction()
                transaction?.replace(R.id.content, fragment)
                transaction?.commit()
            }
            if (preference?.userRoleID.equals(C.GENERAL_MEMBER)) {
                actionTitle?.text = "My Orders"
                fragment = OrderHistoryFragment()
                val transaction = fm?.beginTransaction()
                transaction?.replace(R.id.content, fragment)
                transaction?.commit()
            }
            if (preference?.userRoleID.equals(C.ORGANIZATION)) {
                actionTitle?.text = "My Members"
                fragment = MyMemberFragment()
                val transaction = fm?.beginTransaction()
                transaction?.replace(R.id.content, fragment)
                transaction?.commit()
            }
        } else if (position == 5) {
            img_edit?.visibility = View.GONE
            if (preference?.userRoleID.equals(C.FUNDSPOT)) {
                actionTitle?.text = "My Members"
                fragment = MyMemberFragment()
                val transaction = fm?.beginTransaction()
                transaction?.replace(R.id.content, fragment)
                transaction?.commit()
            }
            if (preference?.userRoleID.equals(C.GENERAL_MEMBER) || preference?.userRoleID.equals(C.ORGANIZATION)) {
                actionTitle?.text = "Saved Cards"
                fragment = MyCardsFragment()
                val transaction = fm?.beginTransaction()
                transaction?.replace(R.id.content, fragment)
                transaction?.commit()
                //TODO SOMTHING
            }

        } else if (position == 6) {
            img_edit?.visibility = View.GONE
            if (preference?.userRoleID.equals(C.GENERAL_MEMBER) || preference?.userRoleID.equals(C.ORGANIZATION)) {
                actionTitle?.text = "Settings"
                fragment = GeneralSettingFragment()
                val transaction = fm?.beginTransaction()
                transaction?.replace(R.id.content, fragment)
                transaction?.commit()
            } else {

                actionTitle?.text = "Saved Cards"
                fragment = MyCardsFragment()
                val transaction = fm?.beginTransaction()
                transaction?.replace(R.id.content, fragment)
                transaction?.commit()
            }

        } else if (position == 7) {
            img_edit?.visibility = View.GONE
            if (preference?.userRoleID.equals(C.FUNDSPOT)) {
                actionTitle?.text = "Settings"
                fragment = GeneralSettingFragment()
                val transaction = fm?.beginTransaction()
                transaction?.replace(R.id.content, fragment)
                transaction?.commit()
            }
            if(preference?.userRoleID.equals(C.GENERAL_MEMBER) || preference?.userRoleID.equals(C.ORGANIZATION))
            {
                val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                //val shareBodyText = GlobalFile.share
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Fundit")
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,"")
                startActivity(Intent.createChooser(sharingIntent, "Share Via"))

            }


        } else if (position == 8) {
            img_edit?.visibility = View.GONE

            if(preference?.userRoleID.equals(C.FUNDSPOT))
            {
                val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                //val shareBodyText = GlobalFile.share
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Fundit")
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,"")
                startActivity(Intent.createChooser(sharingIntent, "Share Via"))

            }

            if(preference?.userRoleID.equals(C.ORGANIZATION))
            {
                actionTitle?.text = "Help"
                fragment = HelpFragment()
                val transaction = fm?.beginTransaction()
                transaction?.replace(R.id.content, fragment)
                transaction?.commit()
            }
            if(preference?.userRoleID.equals(C.GENERAL_MEMBER))
            {
                actionTitle?.text = "Help"
                fragment = HelpFragment()
                val transaction = fm?.beginTransaction()
                transaction?.replace(R.id.content, fragment)
                transaction?.commit()
            }
        } else if (position == 9) {
            img_edit?.visibility = View.GONE
            if (preference?.userRoleID.equals(C.ORGANIZATION) || (preference?.userRoleID.equals(C.GENERAL_MEMBER)))
            {
                logout()
            }
            if(preference?.userRoleID.equals(C.FUNDSPOT))
            {
                actionTitle?.text = "Help"
                fragment = HelpFragment()
                val transaction = fm?.beginTransaction()
                transaction?.replace(R.id.content, fragment)
                transaction?.commit()
            }

        } else if (position == 10) {

            if (preference?.userRoleID.equals(C.FUNDSPOT))
                logout()
        }
        drawerLayout?.closeDrawer(Gravity.START)
    }

    private  fun  coupon()
    {
        actionTitle?.text = "My Orders"
        fragment = OrderHistoryFragment()
        val transaction = fm?.beginTransaction()
        transaction?.replace(R.id.content, fragment)
        transaction?.commit()
        flag =false

    }


    private fun logout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout Application")
        builder.setMessage("Are You sure you want to logout?")
        builder.setCancelable(false)
        builder.setPositiveButton("Confirm") { dialogInterface, i ->

            preference?.isLoggedIn = false
            preference?.userID = ""
            preference?.userRoleID = ""
            preference?.tokenHash = ""
            preference?.userData = ""
            preference?.memberData = ""
            preference?.messageCount = 0
            preference?.isSkiped = false
            preference?.isFirstTime = true

            // preference?.clearData(applicationContext)

            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
            dialogInterface.dismiss()

        }
        builder.setNegativeButton("Cancel"){ dialogInterface, i ->

            dialogInterface.dismiss()

        }
        val bDialog = builder.create()
        bDialog.show()




    }

    public class LeftNavigationAdapter(var context: Activity, var menus: Array<String>, private val preference: AppPreference?) : BaseAdapter() {

        var couponCount: Int = 0


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view = View.inflate(context, R.layout.layout_list_navigation, null)

            val txt_title = view.findViewById(R.id.txt_title) as TextView
            val txt_count = view.findViewById(R.id.txt_count) as TextView

            txt_title.text = menus[position]


            val getTotalCount = preference?.totalCount
            Log.e("getTotalCount" , "-->" + getTotalCount)

            if (position == 2) {
                if(getTotalCount==0){
                    txt_count.visibility = View.GONE
                }else {
                    txt_count.visibility = View.VISIBLE
                    txt_count.text = getTotalCount.toString()
                }
            } else {
                txt_count.visibility = View.GONE
            }


            return view
        }

        override fun getItem(position: Int): Any {
            return menus[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return menus.size
        }

    }

    override fun onBackPressed() {
        /*when (isDrawerOpen) {
            true -> drawerLayout?.closeDrawer(Gravity.START)
            else -> System.exit(0)
        }*/

        if(isDrawerOpen)
            drawerLayout?.closeDrawer(Gravity.START)
        else
           System.exit(0)



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


                C.showToast(applicationContext, "Please check your Internet")


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


                    totalRequest = memberRequest + campaignRequestCount
                    Log.e("totalCount", "--->" + totalRequest)

                    if (cartCount?.text.toString().equals("0")) {
                        cartCount?.visibility = View.GONE
                    } else {
                        cartCount?.visibility = View.VISIBLE
                    }

                    preference?.campaignCount = campaignRequestCount
                    preference?.memberCount = memberRequest
                    preference?.totalCount = totalRequest



                    navigationAdapter?.notifyDataSetChanged()


                    Log.e("count", "" + preference?.messageCount)


                } catch (e: JSONException) {
                    e.printStackTrace()
                }


            }


        }
    }
}
