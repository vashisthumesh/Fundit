package com.fundit.a

import android.app.Activity
import android.content.Context
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.fundit.R
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*
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

    const val TYPE_PRODUCT = "1"
    const val TYPE_GIFTCARD = "2"

    fun findToolbarCenteredText(context: Activity): Toolbar {
        return context.findViewById(R.id.toolbarCenterText) as Toolbar
    }

    fun showToast(context: Context, message: String): Unit {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.layout_toast, null)
        val txt_message = view.findViewById(R.id.txt_message) as TextView
        val toast = Toast(context)
        txt_message.text = message
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

    fun noInternet(context: Context): Unit{
        showToast(context,"No internet connection\nPlease try again later!")
    }

    fun defaultError(context: Context): Unit{
        showToast(context,"Something went wrong\nPlease try again later!")
    }

    fun validEmail(email: String): Boolean{

        val pattern: Pattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{2,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{2,64}" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{2,6}")
        val match: Matcher = pattern.matcher(email)

        return match.matches()
    }

    fun errorToast(context: Context, t: Throwable): Unit{
        Log.e("Error", t.message)
        when(t){
            is ConnectException -> noInternet(context)
            is UnknownHostException -> noInternet(context)
            else -> defaultError(context)
        }
    }

    fun convertDate(currentFormat: String, newFormat: String, date: String): String {

        val sdf = SimpleDateFormat(currentFormat, Locale.getDefault())
        val newForm = SimpleDateFormat(newFormat, Locale.getDefault())
        try {
            return newForm.format(sdf.parse(date))
        } catch (e: Exception) {
            return date
        }
    }


}