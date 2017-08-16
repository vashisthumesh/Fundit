package com.fundit.fragmet


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

import com.fundit.R
import com.fundit.a.AppPreference
import com.fundit.a.C
import com.fundit.adapter.ShowCampaignAdapter
import com.fundit.apis.AdminAPI
import com.fundit.apis.ServiceGenerator
import com.fundit.helper.CustomDialog
import com.fundit.model.CampaignListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class PastCampaignFragment : Fragment() {

    internal lateinit var view: View
    internal lateinit var preference: AppPreference
    internal lateinit var adminAPI: AdminAPI
    internal lateinit var listCampaign: ListView
    internal lateinit var campaignAdapter: ShowCampaignAdapter
    internal lateinit var dialog: CustomDialog

    internal var campaignArrayList: MutableList<CampaignListResponse.CampaignList> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        view = inflater!!.inflate(R.layout.fragment_past_campaign, container, false)
        preference = AppPreference(activity)
        adminAPI = ServiceGenerator.getAPIClass()
        dialog = CustomDialog(activity)


        fetchIDs()

        return view
    }

    private fun fetchIDs() {

        listCampaign = view.findViewById(R.id.list_pastCampaigns) as ListView
        campaignAdapter = ShowCampaignAdapter(campaignArrayList, activity)
        listCampaign.adapter = campaignAdapter


        dialog.show()
        var campaignResponse: Call<CampaignListResponse>? = null

        campaignResponse = adminAPI.ApprovedCampaign(preference.userID, preference.tokenHash, preference.userRoleID, C.PAST)
        campaignResponse!!.enqueue(object : Callback<CampaignListResponse> {
            override fun onResponse(call: Call<CampaignListResponse>, response: Response<CampaignListResponse>) {
                dialog.dismiss()
                val campaignList = response.body()
                if (campaignList != null) {
                    C.showToast(activity, campaignList.message)
                    if (campaignList.isStatus) {
                        campaignArrayList.addAll(campaignList.data)
                    } else {

                        // FOR_NOW_ITS_NOTHING
                    }

                    campaignAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<CampaignListResponse>, t: Throwable) {
                dialog.dismiss()
                C.errorToast(activity, t)

            }
        })


    }
}
