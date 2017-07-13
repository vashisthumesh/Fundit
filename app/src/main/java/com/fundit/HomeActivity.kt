package com.fundit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.fundit.a.C
import com.fundit.fragmet.HomeFragment
import com.fundit.fragmet.MyCampaignsFragment
import com.fundit.fragmet.MyProfileFragment

class HomeActivity : AppCompatActivity() {

    internal var list_navigation: ListView? = null
    var headerView: View? = null
    var navigationAdapter: LeftNavigationAdapter? = null
    var menuList = arrayOf("Home", "My Profile", "My Coupons", "My Orders", "Banks and Cards", "Settings", "Invite Friends", "Help", "Sign In", "Account Type", "Organization A/C", "Verification", "Organization Profile", "General Member Profile", "Fundspot Profile")
    var fragment: Fragment? = null
    var fm: FragmentManager? = null
    var toolbar: Toolbar? = null
    var actionTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        fetchIDs()
        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar = C.findToolbarCenteredText(this)
        actionTitle = findViewById(R.id.actionTitle) as TextView
        //actionTitle?.text = ""
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    private fun fetchIDs() {
        fragment = HomeFragment()
        fm = supportFragmentManager
        val transaction = fm?.beginTransaction()
        transaction?.replace(R.id.content,fragment)
        transaction?.commit()

        list_navigation = findViewById(R.id.list_navigation) as ListView
        headerView = View.inflate(this, R.layout.navigation_drawer, null)
        list_navigation?.addHeaderView(headerView)
        navigationAdapter = LeftNavigationAdapter(this, menuList)
        list_navigation?.adapter = navigationAdapter
        list_navigation?.setOnItemClickListener { adapterView, view, i, l -> handleClicks(i) }
    }

    private fun handleClicks(position: Int) {
        if (position == 0) {

        } else if (position == 1) {
            fragment = HomeFragment()
            val transaction = fm?.beginTransaction()
            transaction?.replace(R.id.content,fragment)
            transaction?.commit()
        } else if (position == 2) {
            fragment = MyProfileFragment()
            val transaction = fm?.beginTransaction()
            transaction?.replace(R.id.content,fragment)
            transaction?.commit()
        } else if (position == 3) {

        } else if (position == 4) {

        } else if (position == 5) {

        } else if (position == 6) {
            var intent = Intent(this,FundraiserSettings::class.java)
            startActivity(intent)
        } else if (position == 7) {

        } else if (position == 8) {

        } else if (position == 9) {
            var intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
        } else if (position == 10) {
            var intent = Intent(this,AccountTypeActivity::class.java)
            startActivity(intent)
        } else if (position == 11) {
            var intent = Intent(this,OrganizationAccountActivity::class.java)
            startActivity(intent)
        } else if (position == 12) {
            var intent = Intent(this,VerificationActivity::class.java)
            startActivity(intent)
        } else if (position == 13) {
            var intent = Intent(this,OrganizationProfileActivity::class.java)
            startActivity(intent)
        } else if (position == 14) {
            var intent = Intent(this,GeneralMemberProfileActivity::class.java)
            startActivity(intent)
        } else if (position == 15) {
            var intent = Intent(this,FundSpotProfile::class.java)
            startActivity(intent)
        }
    }

    data class LeftNavigationAdapter(var context: Activity, var menus: Array<String>) : BaseAdapter() {

        var couponCount: Int = 0

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view = View.inflate(context, R.layout.layout_list_navigation, null)

            val txt_title = view.findViewById(R.id.txt_title) as TextView
            val txt_count = view.findViewById(R.id.txt_count) as TextView

            txt_title.text = menus[position]

            if (position == 2 && couponCount > 0) {
                txt_count.visibility = View.VISIBLE
                if (couponCount > 99) txt_count.text = "99+"
                else txt_count.text = couponCount.toString()
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

}
