package com.duwamish.radio.transmitter.data

import android.bluetooth.BluetoothDevice
import java.time.LocalDateTime

data class Beacon(val uuid: String,
                  val major: Int,
                  val minor: Int,
                  val rsStrengthIndicator: Int,
                  val measuredPower: Int,
                  val lastDetected: LocalDateTime,
                  val underlyingDevice: BluetoothDevice,
                  val protocol: String
) {

    /**
     * https://github.com/AltBeacon/android-beacon-library/blob/master/lib/src/main/java/org/altbeacon/beacon/distance/CurveFittedDistanceCalculator.java#L60
     */
    fun estimatedDistance(): Double {
        val n = 2
        val ratio = (measuredPower.toDouble() - rsStrengthIndicator) / (10 * n)
        return Math.pow(10.0, ratio)
    }
}
