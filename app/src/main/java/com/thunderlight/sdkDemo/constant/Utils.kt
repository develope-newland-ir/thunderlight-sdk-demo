package com.thunderlight.sdkDemo.constant

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log


/**
 * @author Created by M.Moradikia
 * @date  2/19/2023
 * @company Thunder-Light
 */

fun Bundle.toString2(): String {
    val TAG = "Bundle.toString"
    var x = ""
    try {
        val bundle = this
        if (bundle != null) {
            for (key in bundle.keySet()) {
                Log.e(TAG, key + " : " + if (bundle[key] != null) bundle[key] else "NULL")
                x += "\n" + key + " : " + if (bundle[key] != null) bundle[key] else "NULL"
            }
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return x
}

fun getVersion(context: Context): String {
    var version = ""
    try {
        val pInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        version = pInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return version
}