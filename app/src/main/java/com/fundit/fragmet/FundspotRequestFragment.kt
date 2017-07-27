package com.fundit.fragmet

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.fundit.R
import com.fundit.a.AppPreference
import com.fundit.a.C
import com.fundit.adapter.CampaignRequestAdapter
import com.fundit.apis.AdminAPI
import com.fundit.apis.ServiceGenerator
import com.fundit.helper.CustomDialog
import com.fundit.model.CampaignListResponse
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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fview = inflater?.inflate(R.layout.fragment_campaign_request, container, false)

        preference = AppPreference(activity)
        adminAPI = ServiceGenerator.getAPIClass()
        dialog = CustomDialog(activity)
        fetchIDs()

        return fview
    }

    private fun fetchIDs() {
        listRequests = fview?.findViewById(R.id.listRequests) as ListView
        campaignRequestAdapter = CampaignRequestAdapter(campaignList, activity)
        listRequests?.adapter = campaignRequestAdapter

        dialog?.show()
        var campaignCall: Call<CampaignListResponse>? = null
        when (preference?.userRoleID) {
            C.ORGANIZATION -> {
                campaignCall = adminAPI?.getAllCampaigns(preference?.userID, preference?.tokenHash, preference?.userRoleID, preference?.userID, null)

            }
            C.FUNDSPOT -> {
                campaignCall = adminAPI?.getAllCampaigns(preference?.userID, preference?.tokenHash, preference?.userRoleID, null, preference?.userID)
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
}