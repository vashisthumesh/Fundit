package com.fundit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
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
import com.fundit.fragmet.FRequestFragment
import com.fundit.fragmet.HomeFragment
import com.fundit.fragmet.MyProfileFragment
import com.fundit.fundspot.MyProductsFragment
import com.fundit.model.Fundspot
import com.fundit.model.Member
import com.fundit.model.Organization
import com.fundit.model.User
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        preference = AppPreference(this)

        fillMenus()
        setupToolbar()
        fetchIDs()

    }

    private fun fillMenus() {
        when (preference?.userRoleID) {
            C.ORGANIZATION -> {
                menuList = arrayOf("Home", "My Profile", "Requests", "Banks and Cards", "Settings", "Invite Friends", "Help", "Logout")
            }
            C.FUNDSPOT -> {
                menuList = arrayOf("Home", "My Profile", "Requests", "Banks and Cards", "Settings", "Invite Friends", "Help", "Logout")
            }
            C.GENERAL_MEMBER -> {
                menuList = arrayOf("Home", "My Profile", "My Coupons", "My Orders", "Banks and Cards", "Settings", "Invite Friends", "Help", "Logout")
            }
        }
    }

    private fun setupToolbar() {
        toolbar = C.findToolbarCenteredText(this)
        actionTitle = findViewById(R.id.actionTitle) as TextView
        img_edit = findViewById(R.id.img_edit) as ImageView
        img_notification =findViewById(R.id.img_notification) as ImageView
        //actionTitle?.text = ""
        setSupportActionBar(toolbar)

        img_notification.setOnClickListener{
            val intent = Intent(this@HomeActivity , NotificationActivity::class.java)
            startActivity(intent)
        }
        img_edit.setOnClickListener {

            val editMode: Boolean = true
            when (preference?.userRoleID) {
                C.ORGANIZATION -> {

                    val intent = Intent(this@HomeActivity , OrganizationProfileActivity::class.java)
                    intent.putExtra("isEdiMode" , editMode)
                    startActivity(intent)


                } C.FUNDSPOT -> {


                    val intent = Intent(this@HomeActivity , FundSpotProfile::class.java)
                    intent.putExtra("isEditMode" , editMode)
                    startActivity(intent)


                } C.GENERAL_MEMBER -> {


                    val intent = Intent(this@HomeActivity , GeneralMemberProfileActivity::class.java)
                    intent.putExtra("isEditMode" , editMode)
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
            C.FUNDSPOT -> fragment = MyProductsFragment()
            C.GENERAL_MEMBER -> fragment = Fragment()
            else -> fragment = Fragment()
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
        val user: User = gson.fromJson(preference?.userData, User::class.java)
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

        navigationAdapter = LeftNavigationAdapter(this, menuList)
        list_navigation?.adapter = navigationAdapter
        list_navigation?.setOnItemClickListener { _, _, i, _ -> handleClicks(i) }
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
                    fragment = MyProductsFragment()
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
            actionTitle?.text = "Requests"
            fragment = FRequestFragment()
            val transaction = fm?.beginTransaction()
            transaction?.replace(R.id.content, fragment)
            transaction?.commit()
        } else if (position == 4) {

        } else if (position == 5) {

        } else if (position == 6) {
            val intent = Intent(this, FundraiserSettings::class.java)
            startActivity(intent)
        } else if (position == 7) {

        } else if (position == 8) {
            if (preference?.userRoleID.equals(C.ORGANIZATION) || preference?.userRoleID.equals(C.FUNDSPOT)) {
                logout()
            } else {
                //TODO SOMETHING
            }

        } else if (position == 9) {
            logout()
        }
        drawerLayout?.closeDrawer(Gravity.START)
    }

    private fun logout() {
        preference?.isLoggedIn = false
        preference?.userID = ""
        preference?.userRoleID = ""
        preference?.tokenHash = ""
        preference?.userData = ""
        preference?.memberData = ""

        val intent = Intent(this, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
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

    override fun onBackPressed() {
        when (isDrawerOpen) {
            true -> drawerLayout?.closeDrawer(Gravity.START)
            else -> System.exit(0)
        }
    }

}
