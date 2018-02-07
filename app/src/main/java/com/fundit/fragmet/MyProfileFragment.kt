package com.fundit.fragmet

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
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
import com.fundit.apis.AdminAPI
import com.fundit.apis.ServiceGenerator
import com.fundit.helper.CustomDialog
import com.fundit.model.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileFragment : Fragment() {

    var preference: AppPreference? = null
    var img_profilePic: CircleImageView? = null
    var txt_name: TextView? = null
    var txt_address: TextView? = null
    var txt_fundspots: TextView? = null
    var txt_organizations: TextView? = null
    var txt_emailID: TextView? = null
    var txt_con_info_email:TextView?=null
    var txt_con_info_mobile:TextView?=null
    var txt_description_data:TextView?=null
   var txt_fundraiser_split:TextView?=null
    var txt_category:TextView?=null
    var type:TextView?=null
    var txt_type:TextView?=null


    lateinit var txt_change_pwd: TextView
    var layout_type:LinearLayout?=null
    var layout_org: LinearLayout? = null
    var layout_fun: LinearLayout? = null
    var layout_fundraiser:LinearLayout?=null
    var layout_category:LinearLayout?=null
    var layout_description:LinearLayout?=null
    var layout_contact_info_mobile:LinearLayout?=null
    var layout_contact_info_email:LinearLayout?=null
    var layout_buttons:LinearLayout?=null
    var dialog: CustomDialog? = null
    var adminAPI: AdminAPI? = null
    internal var member = Member()



    var btn_edit: Button? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_my_profile, container, false)


        adminAPI = ServiceGenerator.getAPIClass()
        preference = AppPreference(activity)
        dialog = CustomDialog(activity)
        img_profilePic = view.findViewById(R.id.img_profilePic) as CircleImageView
        txt_name = view.findViewById(R.id.txt_name) as TextView
        txt_address = view.findViewById(R.id.txt_address) as TextView
        txt_emailID = view.findViewById(R.id.txt_emailID) as TextView
        txt_change_pwd = view.findViewById(R.id.txt_change_pwd) as TextView
        txt_fundspots = view.findViewById(R.id.txt_fundspots) as TextView
        txt_organizations = view.findViewById(R.id.txt_organizations) as TextView
        txt_con_info_email=view.findViewById(R.id.txt_con_info_email) as TextView
        txt_con_info_mobile=view.findViewById(R.id.txt_con_info_mobile) as TextView
        txt_description_data= view.findViewById(R.id.txt_description_data) as TextView
        txt_fundraiser_split= view.findViewById(R.id.txt_fundraiser_split) as TextView
        txt_category= view.findViewById(R.id.txt_category) as TextView
        type= view.findViewById(R.id.type) as TextView
        txt_type= view.findViewById(R.id.txt_type) as TextView



        layout_type= view.findViewById(R.id.layout_type) as LinearLayout?
        layout_org = view.findViewById(R.id.layout_org) as LinearLayout
        layout_fun = view.findViewById(R.id.layout_fun) as LinearLayout
        layout_description= view.findViewById(R.id.layout_description) as LinearLayout
        layout_fundraiser= view.findViewById(R.id.layout_fundraiser) as LinearLayout
        layout_category= view.findViewById(R.id.layout_category) as LinearLayout
        layout_buttons= view.findViewById(R.id.layout_buttons) as LinearLayout
        layout_buttons?.visibility=View.GONE


        layout_contact_info_mobile=view.findViewById(R.id.layout_contact_info_mobile) as LinearLayout
        layout_contact_info_email=view.findViewById(R.id.layout_contact_info_email) as LinearLayout


        val userData = Gson().fromJson(preference?.userData, User::class.java)
        member = Gson().fromJson(preference?.getMemberData(), Member::class.java)




        var imagePath: String = ""

        when (preference?.userRoleID) {
            C.ORGANIZATION -> {
                val memberData = Gson().fromJson(preference?.memberData, Organization::class.java)
                layout_category?.visibility=View.GONE
                layout_fundraiser?.visibility=View.GONE
                layout_type?.visibility=View.GONE




                txt_name?.text = userData.title
                txt_emailID?.text = userData.email_id


                if(memberData.description.isEmpty())
                {
                    layout_description?.visibility=View.GONE
                }
                else{
                    layout_description?.visibility=View.VISIBLE
                    txt_description_data?.text=memberData.description
                }


                /*if(memberData.type.name.isEmpty() || memberData.subType.name.isEmpty())
                {
                    layout_type?.visibility=View.GONE

                }
                else{
                    layout_type?.visibility=View.VISIBLE
                    type?.text=memberData.type.name
                    txt_type?.text=memberData.subType.name
                }
*/
                if(memberData.contact_info_email.isEmpty())
                {
                    layout_contact_info_email?.visibility=View.GONE
                }
                else{
                    layout_contact_info_email?.visibility=View.VISIBLE
                    txt_con_info_email?.text=memberData.contact_info_email
                }

                if(memberData.contact_info_mobile.isEmpty())
                {
                    layout_contact_info_mobile?.visibility=View.GONE
                }
                else
                {
                    layout_contact_info_mobile?.visibility=View.VISIBLE
                    txt_con_info_mobile?.text=memberData.contact_info_mobile
                }

                Log.e("datas" , "-->" + preference?.memberData)


                txt_address?.text = memberData.location + "\n" + memberData.city_name + ", " + memberData.state.state_code + "  " + memberData.zip_code
                layout_fun?.visibility = View.GONE
                layout_org?.visibility = View.GONE
                Log.e("state_code",memberData.state.state_code);
                imagePath = memberData.image

                Picasso.with(context)
                        .load(W.FILE_URL + imagePath)
                        .into(img_profilePic)
            }
            C.FUNDSPOT -> {
                val memberData = Gson().fromJson(preference?.memberData, Fundspot::class.java)

                Log.e("fundsdata" , "-->" + preference?.memberData)
                txt_name?.text = userData.title
                txt_emailID?.text = userData.email_id

                layout_type?.visibility=View.GONE
                layout_category?.visibility=View.GONE

                if(memberData.contact_info_email.isEmpty())
                {
                    layout_contact_info_email?.visibility=View.GONE
                }
                else{
                    layout_contact_info_email?.visibility=View.VISIBLE
                    txt_con_info_email?.text=memberData.contact_info_email
                }

                if(memberData.contact_info_mobile.isEmpty())
                {
                    layout_contact_info_mobile?.visibility=View.GONE
                }
                else
                {
                    layout_contact_info_mobile?.visibility=View.VISIBLE
                    txt_con_info_mobile?.text=memberData.contact_info_mobile
                }


                txt_address?.text = memberData.location + "\n" + memberData.city_name + " , " + memberData.state.state_code + "   " + memberData.zip_code

                /*if(memberData.category_id.isEmpty())
                {
                    layout_category?.visibility=View.GONE
                }
                else{
                    layout_category?.visibility=View.VISIBLE
                    txt_category?.text="Category"+" "+memberData.category_id
                }
*/

                if(memberData.fundraise_split.isEmpty())
                {
                    layout_fundraiser?.visibility=View.GONE
                }
                else{
                    layout_fundraiser?.visibility=View.VISIBLE
                    txt_fundraiser_split?.text=memberData.fundraise_split
                }

                if(memberData.description.isEmpty())
                {
                    layout_description?.visibility=View.GONE
                }
                else{
                    layout_description?.visibility=View.VISIBLE
                    txt_description_data?.text=memberData.description
                }

                layout_fun?.visibility = View.GONE
                layout_org?.visibility = View.GONE
                imagePath = memberData.image

                Picasso.with(context)
                        .load(W.FILE_URL + imagePath)
                        .into(img_profilePic)
            }
//            C.GENERAL_MEMBER -> {
//                val memberData = Gson().fromJson(preference?.memberData, Member::class.java)
//                if(memberData.contact_info_email.isEmpty())
//                {
//                    layout_contact_info_email?.visibility=View.GONE
//                }
//                else{
//                    layout_contact_info_email?.visibility=View.VISIBLE
//                    txt_con_info_email?.text=memberData.contact_info_email
//                }
//
//                if(memberData.contact_info_mobile.isEmpty())
//                {
//                    layout_contact_info_mobile?.visibility=View.GONE
//                }
//                else
//                {
//                    layout_contact_info_mobile?.visibility=View.VISIBLE
//                    txt_con_info_mobile?.text=memberData.contact_info_mobile
//                }
//
//                txt_con_info_mobile?.text=memberData.contact_info_mobile
//                Log.e("datas" , "-->" + preference?.memberData)
//                txt_address?.text = memberData.location + "\n" + memberData.city.name + " , " + memberData.state.state_code+ "  " + memberData.zip_code
//
//               // Log.e("state_code",""+memberData.state.state_code);
//                imagePath = memberData.image
//
//                if(!memberData.fundspot.title.isEmpty()){
//                    layout_fun?.visibility = View.VISIBLE
//                    txt_fundspots?.text = memberData.fundspot.title
//                }
//
//                if(!memberData.organization.title.isEmpty()){
//                    layout_org?.visibility = View.VISIBLE
//                    txt_organizations?.text = memberData.organization.title
//                }
//
//
//
//            }
        }





        /*txt_change_pwd.setOnClickListener{
            val intent = Intent(activity , ChangePasswordActivity::class.java)
            startActivity(intent)
        }
*/


        if(preference?.userRoleID.equals(C.GENERAL_MEMBER))
        {
            layout_category?.visibility=View.GONE
            layout_fundraiser?.visibility=View.GONE
            layout_description?.visibility=View.GONE
            layout_type?.visibility=View.GONE


            dialog?.show()
            var generalResponse: Call<GeneralMemberProfileResponse>? = null

                generalResponse = adminAPI?.ViewGeneralMemberProfile(preference?.userID, preference?.tokenHash, member.id)


            generalResponse!!.enqueue(object : Callback<GeneralMemberProfileResponse> {
                override fun onResponse(call: Call<GeneralMemberProfileResponse>, response: Response<GeneralMemberProfileResponse>)              {
                    dialog?.dismiss()
                    val campaignList = response.body()
                    if (campaignList != null) {
                      //  C.showToast(activity, campaignList.message)
                        if (campaignList.isStatus) {

                            val first_name =campaignList.data.member.first_name
                            val last_name=campaignList.data.member.last_name

                            txt_name?.text = first_name + last_name
                            txt_emailID?.text = campaignList.data.user.email_id
                            txt_address?.text = campaignList.data.member.location + "\n" + campaignList.data.member.city_name + " , " + campaignList.data.state.state_code+ "  " + campaignList.data.member.zip_code

                            if(campaignList.data.member.contact_info_email.isEmpty())
                            {
                                layout_contact_info_email?.visibility=View.GONE
                            }
                            else{
                                layout_contact_info_email?.visibility=View.VISIBLE
                                txt_con_info_email?.text=campaignList.data.member.contact_info_email
                            }

                            if(campaignList.data.member.contact_info_mobile.isEmpty())
                            {
                                layout_contact_info_mobile?.visibility=View.GONE
                            }
                            else
                            {
                                layout_contact_info_mobile?.visibility=View.VISIBLE
                                txt_con_info_mobile?.text=campaignList.data.member.contact_info_mobile
                            }



                            if(campaignList.data.member.fundspot_names.isEmpty()){
                                layout_fun?.visibility = View.GONE

                            }else {
                                layout_fun?.visibility = View.VISIBLE
                                txt_fundspots?.text = campaignList.data.member.fundspot_names
                            }

                            if(campaignList.data.member.organization_names.isEmpty()){
                                layout_org?.visibility = View.GONE

                            }else {
                                layout_org?.visibility = View.VISIBLE
                                txt_organizations?.text = campaignList.data.member.organization_names
                            }

                            imagePath = campaignList.data.member.image
                            Picasso.with(context)
                                    .load(W.FILE_URL + imagePath)
                                    .into(img_profilePic)


                        } else {

                            // FOR_NOW_ITS_NOTHING
                        }


                    }
                }

                override fun onFailure(call: Call<GeneralMemberProfileResponse>, t: Throwable) {
                    dialog?.dismiss()
                    C.errorToast(activity, t)

                }
            })


        }

        return view
    }
}
