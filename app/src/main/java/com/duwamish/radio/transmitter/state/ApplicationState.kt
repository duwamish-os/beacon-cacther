package com.duwamish.radio.transmitter.state

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager

class ApplicationState {
    companion object {
        lateinit var bluetoothManager: BluetoothManager
        lateinit var bluetoothAdapter: BluetoothAdapter
    }
}
