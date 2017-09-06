package com.fundit.fragmet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.fundit.CreateCampaignTermsActivity
import com.fundit.R
import com.fundit.a.AppPreference
import com.fundit.a.C
import com.fundit.adapter.CampaignRequestAdapter
import com.fundit.apis.AdminAPI
import com.fundit.apis.ServiceGenerator
import com.fundit.helper.CustomDialog
import com.fundit.model.*
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Nivida new on 25-Jul-17.
 */
class FundspotRequestFragment : Fragment() {

    var fview: View? = null
    var listRequests: ListView? = null
    var campaignRequestAdapter: CampaignRequestAdapter? = null
    var campaignList: MutableList<CampaignListResponse.CampaignList> = ArrayList()
    var adminAPI: AdminAPI? = null
    var preference: AppPreference? = null
    var dialog: CustomDialog? = null
    val SUCCESS_CODE: Int = 369

    internal var user = User()
    internal var member = Member()
    internal var organization = Organization()
    internal var fundspot = Fundspot()

    var requestLayout: LinearLayout? = null
    var  textCount: TextView?= null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fview = inflater?.inflate(R.layout.fragment_campaign_request, container, false)

        preference = AppPreference(activity)
        adminAPI = ServiceGenerator.getAPIClass()
        dialog = CustomDialog(activity)

        try {
           user = Gson().fromJson(preference?.userData, User::class.java)
            member = Gson().fromJson(preference?.memberData, Member::class.java)
            organization = Gson().fromJson(preference?.memberData, Organization::class.java)
            fundspot = Gson().fromJson(preference?.memberData , Fundspot::class.java)


            Log.e("userData","-->" +preference?.userData)
            Log.e("userData","--->"+ preference?.memberData)
        } catch (e: Exception) {
            Log.e("Exception", e.message)
        }



        fetchIDs()

        return fview
    }

    private fun fetchIDs() {
        listRequests = fview?.findViewById(R.id.listRequests) as ListView

        textCount = fview?.findViewById(R.id.txt_count) as TextView
        requestLayout = fview?.findViewById(R.id.layout_request) as LinearLayout

        var getTotalCount = preference?.campaignCount

        if(getTotalCount!! <= 0){
            requestLayout?.visibility = View.GONE
        }
        else {
            textCount?.text = getTotalCount.toString()
        }



        campaignRequestAdapter = CampaignRequestAdapter(campaignList, activity,object : CampaignRequestAdapter.OnReviewClickListener{
            override fun onReviewButtonClick(position: Int) {

                val intent = Intent(activity,CreateCampaignTermsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP ; Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("campaignItem",campaignList.get(position))
                startActivityForResult(intent,SUCCESS_CODE)
            }
        })
        listRequests?.adapter = campaignRequestAdapter

        showCampaignRequests()

    }

    fun showCampaignRequests(){
        dialog?.show()
        campaignList.clear()
        campaignRequestAdapter?.notifyDataSetChanged()
        var campaignCall: Call<CampaignListResponse>? = null
        when (preference?.userRoleID) {
            C.ORGANIZATION -> {
                campaignCall = adminAPI?.getAllCampaigns(preference?.userID, preference?.tokenHash, preference?.userRoleID, preference?.userID, null,C.INACTIVE,null)



            }
            C.FUNDSPOT -> {
                campaignCall = adminAPI?.getAllCampaigns(preference?.userID, preference?.tokenHash, preference?.userRoleID, null,preference?.userID ,C.INACTIVE,C.PENDING)


            }
        }
        campaignCall?.enqueue(object : Callback<CampaignListResponse> {

            override fun onResponse(call: Call<CampaignListResponse>?, response: Response<CampaignListResponse>?) {
                dialog?.dismiss()
                val campaignListResponse = response?.body()
                when (campaignListResponse) {
                    null -> C.defaultError(context)
                    else -> {
                        when (campaignListResponse.isStatus) {
                            false -> C.showToast(context, campaignListResponse.message)
                            true -> {
                                campaignList.addAll(campaignListResponse.data)
                            }
                        }
                    }
                }
                campaignRequestAdapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<CampaignListResponse>?, t: Throwable) {
                dialog?.dismiss()
                C.errorToast(context, t)
            }
        })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("ActivityF","Result"+ requestCode +" - "+ resultCode);

        when(requestCode){
            SUCCESS_CODE -> {
                when(resultCode){
                    Activity.RESULT_OK -> {
                        showCampaignRequests()
                    }
                }
            }
        }
    }


}