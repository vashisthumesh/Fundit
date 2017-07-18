package com.fundit.a

import android.app.Activity
import android.content.Context
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.fundit.R
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Nivida new on 12-Jul-17.
 */
object C{

    const val ORGANIZATION: String = "2"
    const val FUNDSPOT: String = "3"
    const val GENERAL_MEMBER: String = "4"

    const val VERIFIED:String ="1"

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

    fun validEmail(email: String): Boolean{

        val pattern: Pattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\" + \"\\\\@\" + \"[a-zA-Z0-9][a-zA-Z0-9\\\\-]{1,64}\" + \"(\" +\n" + "\"\\\\.\" + \"[a-zA-Z0-9][a-zA-Z0-9\\-]{2,6}",Pattern.CASE_INSENSITIVE)
        val match: Matcher = pattern.matcher(email)

        return match.matches()
    }

    fun errorToast(context: Context, t: Throwable): Unit{
        when(t){
            is ConnectException -> noInternet(context)
            is UnknownHostException -> noInternet(context)
            else -> defaultError(context)
        }
    }


}