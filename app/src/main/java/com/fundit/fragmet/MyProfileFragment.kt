package com.fundit.fragmet

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.fundit.ChangePasswordActivity
import com.fundit.OrganizationProfileActivity
import com.fundit.R
import com.fundit.a.AppPreference
import com.fundit.a.C
import com.fundit.a.W
import com.fundit.model.Fundspot
import com.fundit.model.Member
import com.fundit.model.Organization
import com.fundit.model.User
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MyProfileFragment : Fragment() {

    var preference: AppPreference? = null
    var img_profilePic: CircleImageView? = null
    var txt_name: TextView? = null
    var txt_address: TextView? = null
    var txt_fundspots: TextView? = null
    var txt_organizations: TextView? = null
    var txt_emailID: TextView? = null
    lateinit var txt_change_pwd: TextView

    var layout_org: LinearLayout? = null
    var layout_fun: LinearLayout? = null

    var btn_edit: Button? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_my_profile, container, false)

        preference = AppPreference(activity)

        img_profilePic = view.findViewById(R.id.img_profilePic) as CircleImageView
        txt_name = view.findViewById(R.id.txt_name) as TextView
        txt_address = view.findViewById(R.id.txt_address) as TextView
        txt_emailID = view.findViewById(R.id.txt_emailID) as TextView
        txt_change_pwd = view.findViewById(R.id.txt_change_pwd) as TextView
        txt_fundspots = view.findViewById(R.id.txt_fundspots) as TextView
        txt_organizations = view.findViewById(R.id.txt_organizations) as TextView


        layout_org = view.findViewById(R.id.layout_org) as LinearLayout
        layout_fun = view.findViewById(R.id.layout_fun) as LinearLayout


        val userData = Gson().fromJson(preference?.userData, User::class.java)

        txt_name?.text = userData.title
        txt_emailID?.text = userData.email_id


        var imagePath: String = ""

        when (preference?.userRoleID) {
            C.ORGANIZATION -> {
                val memberData = Gson().fromJson(preference?.memberData, Organization::class.java)
                txt_address?.text = memberData.location
                imagePath = memberData.image
            }
            C.FUNDSPOT -> {
                val memberData = Gson().fromJson(preference?.memberData, Fundspot::class.java)
                txt_address?.text = memberData.location
                imagePath = memberData.image
            }
            C.GENERAL_MEMBER -> {
                val memberData = Gson().fromJson(preference?.memberData, Member::class.java)
                txt_address?.text = memberData.location
                imagePath = memberData.image

                if(!memberData.fundspot.title.isEmpty()){
                    layout_fun?.visibility = View.VISIBLE
                    txt_fundspots?.text = memberData.fundspot.title
                }

                if(!memberData.organization.title.isEmpty()){
                    layout_org?.visibility = View.VISIBLE
                    txt_organizations?.text = memberData.organization.title
                }

            }
        }


        Picasso.with(context)
                .load(W.FILE_URL + imagePath)
                .into(img_profilePic)


        /*txt_change_pwd.setOnClickListener{
            val intent = Intent(activity , ChangePasswordActivity::class.java)
            startActivity(intent)
        }
*/

        return view
    }
}
