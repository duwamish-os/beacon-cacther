package com.duwamish.radio.transmitter.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.duwamish.radio.transmitter.R

public class BleRadarTab(): Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.ble_radar_tab, container, false)

        return view
    }
}
