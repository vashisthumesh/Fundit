package com.fundit.fragmet


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.fundit.R

/**
 * A simple [Fragment] subclass.
 */
class PastCampaignFragment : Fragment() {

    internal var view: View? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater!!.inflate(R.layout.fragment_past_campaign, container, false)



        return view
    }

}
