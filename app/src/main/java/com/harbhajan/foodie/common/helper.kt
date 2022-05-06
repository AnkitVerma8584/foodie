package com.harbhajan.foodie.common

import android.util.Log

fun Any?.printLog(tag: String = "TAG") {
    Log.e(tag, this.toString())
}

const val CHANNEL_ONE_ID = "101"
const val CHANNEL_ONE_NAME = "New Orders"
const val CHANNEL_ONE_DESCRIPTION = "Receive notifications for new orders and updates."
