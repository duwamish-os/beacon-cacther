package com.duwamish.radio.transmitter.view

import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.duwamish.radio.transmitter.R
import java.util.*

public class UserInfoTab(): Fragment () {

    private lateinit var userId: TextView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.ble_user_info, container, false)
        userId = view.findViewById<TextView>(R.id.userId)

        var androidId = Settings.Secure.getString(
                getContext()?.getContentResolver(),
                Settings.Secure.ANDROID_ID
        )
//
//        if(androidId != null)
//            androidId = androidId + "00000"
//        else
//            androidId = "" + "00000"
//        val key= androidId.substring(0, Math.min(androidId.length, 5))

        userId.text = (androidId)

        return view
    }
}
