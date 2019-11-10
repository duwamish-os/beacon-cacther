package com.duwamish.radio.transmitter.data

import android.bluetooth.BluetoothDevice
import java.time.LocalDateTime

data class Beacon(val uuid: String,
                  val major: Int,
                  val minor: Int,
                  val rsStrengthIndicator: Int,
                  val measuredPower: Int,
                  val estimatedDistance: Double,
                  val lastDetected: LocalDateTime,
                  val underlyingDevice: BluetoothDevice,
                  val layout: String
)
