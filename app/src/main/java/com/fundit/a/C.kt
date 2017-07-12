package com.fundit.a

import android.app.Activity
import android.content.Context
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.fundit.R

/**
 * Created by Nivida new on 12-Jul-17.
 */
object C{

    fun findToolbarCenteredText(context: Activity): Toolbar {
        return context.findViewById(R.id.toolbarCenterText) as Toolbar
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }
}