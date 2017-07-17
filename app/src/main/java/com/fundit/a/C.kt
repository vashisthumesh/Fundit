package com.fundit.a

import android.app.Activity
import android.content.Context
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.fundit.R
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * Created by Nivida new on 12-Jul-17.
 */
class C{

    fun findToolbarCenteredText(context: Activity): Toolbar {
        return context.findViewById(R.id.toolbarCenterText) as Toolbar
    }

    fun showToast(context: Context, message: String): Unit {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    fun noInternet(context: Context): Unit{
        showToast(context,"No internet connection\nPlease try again later!")
    }

    fun defaultError(context: Context): Unit{
        showToast(context,"Something went wrong\nPlease try again later!")
    }

    open fun errorToast(context: Context, t: Throwable): Unit{
        when(t){
            is ConnectException -> noInternet(context)
            is UnknownHostException -> noInternet(context)
            else -> defaultError(context)
        }
    }

    var ORGANIZATION: String = "2"
    var FUNDSPOT: String = "3"
    var GENERAL_MEMBER: String = "4"
}