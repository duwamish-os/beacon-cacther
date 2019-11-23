package com.duwamish.radio.transmitter.data

import android.bluetooth.BluetoothDevice
import java.math.BigDecimal
import java.math.RoundingMode
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
     * https://stackoverflow.com/q/52962218/432903
     * pow(10, (calibratedRssi - rssi) / (10 * pathLossParameter))
     */
    fun estimatedDistance(): Double {
        val pathLossParam = 2
        val ratio = (measuredPower.toDouble() - rsStrengthIndicator) / (10 * pathLossParam)
        val ed = Math.pow(10.0, ratio)

        return trimmedDistance(ed)
    }

    private fun trimmedDistance(ed: Double): Double {
        val bd = BigDecimal(ed).setScale(4, RoundingMode.HALF_UP)
        return bd.toDouble()
    }

    /**
     * https://github.com/AltBeacon/android-beacon-library/blob/b9876b45acd6c72c84b8d0325062259020eb89ba/lib/src/main/resources/model-distance-calculations.json
     */
    fun estimatedDistanceV2(): Double {
        val x = 0.42093;
        val y = 6.9476;
        val z = 0.549;

        val ratio = (rsStrengthIndicator * 1.0) / measuredPower

        if (ratio < 1.0) {
            return trimmedDistance(Math.pow(10.0, ratio));
        } else {
            return trimmedDistance((x) * Math.pow(ratio, y) + z)
        }
    }
}
