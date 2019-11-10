package com.duwamish.radio.transmitter.view

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.duwamish.radio.transmitter.R
import com.duwamish.radio.transmitter.controller.ScanState
import com.duwamish.radio.transmitter.state.ApplicationState

class BleListTab() : Fragment() {

    private lateinit var beaconsView: ListView
    private val scanHandler = Handler()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.ble_list_tab, container, false)
        beaconsView = view.findViewById<ListView>(R.id.beacons)

        val cx = getContext()
        if ( cx != null) {
            scanHandler.post(ScanState.scanThread(scanHandler, beaconsView, cx, ApplicationState.bluetoothAdapter))
        }

        return view
    }
}
