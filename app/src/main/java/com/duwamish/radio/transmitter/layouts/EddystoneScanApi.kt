package com.duwamish.radio.transmitter.layouts

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.os.ParcelUuid
import android.util.Base64
import android.util.Log
import com.duwamish.radio.transmitter.data.Beacon
import com.duwamish.radio.transmitter.Hex
import java.time.LocalDateTime
import java.util.*

/**
 * https://github.com/google/eddystone/tree/master/eddystone-uid
 *
 * Note to developers: the best way to determine the precise value to put into this field is to
 * measure the actual output of your beacon from 1 meter away and then add 41 dBm to that.
 *
 * 41dBm is the signal loss that occurs over 1 meter.
 */
public class EddystoneScanApi {

    companion object {

        private val PROTOCAL = "eddystone"

        private val LOG_KEY = this.javaClass.name
        private val EDDYSTONE_UUID = ParcelUuid.fromString("0000feaa-0000-1000-8000-00805f9b34fb")
        private val UID_FRAME_TYPE = 0

        /**
         * https://support.kontakt.io/hc/en-gb/articles/201621521-Transmission-power-Range-and-RSSI
         */
        private val RSSI_TO_MEASURED_POWER = linkedMapOf<Int, Int>(
                -99 to -85,
                -23 to -85,
                -21 to -81,
                -18 to -78,
                -15 to -76,
                -12 to -74,
                -9 to -69,
                -6 to -67,
                -3 to -64,
                0 to -62,
                1 to -61,
                2 to -61,
                4 to -60,
                5 to -58,
                99 to -58
        )

        fun scan(device: BluetoothDevice,
                 signalStrength: Int,
                 transmittedPower: Int,
                 bleResult: ScanResult): Beacon? {

            Log.i(LOG_KEY, "validating if eddystone for scanResult ${bleResult.scanRecord}")

            if (bleResult == null) return null

            if (bleResult.scanRecord != null) {
                val serviceUuids = bleResult.scanRecord.serviceUuids
                val manufacturerData = bleResult.scanRecord.manufacturerSpecificData
                val serviceData = bleResult.scanRecord.serviceData
                val address = bleResult.device.address

                if (serviceUuids != null && serviceUuids.contains(EDDYSTONE_UUID)) {
                    Log.i(LOG_KEY, "Eddystone found: ${serviceUuids} which is always same")
                    val namespaceMetadata: ByteArray? = serviceData.get(EDDYSTONE_UUID)
                    Log.i(LOG_KEY, "Eddystone namespace: ${namespaceMetadata}")

                    if (namespaceMetadata != null) {
                        val firstByte = namespaceMetadata.get(0).toInt()
                        when (firstByte) {
                            UID_FRAME_TYPE -> {
                                val measuredPowerAt_0_m = namespaceMetadata.get(1).toInt()
                                val ns: String = Base64.encodeToString(
                                        Arrays.copyOfRange(namespaceMetadata, 2, 18), 2
                                )

//                                val namespaceIndex = ns.lastIndexOf(58.toChar())
//                                if (namespaceIndex != -1) {
//                                    ns.substring(0, namespaceIndex + 1)
//                                    val ns1 = ns.substring(namespaceIndex + 1).split(";")[0]
                                val namespaceId_10_bytes2 = Hex.toHexString(Base64.decode(ns, 2), 0, 10)
                                //F86410C4C588A9CEC5F100 90
                                val namespaceId_10_bytes =
                                        Hex.toHexStringv3(Hex.copyOfRange(namespaceMetadata, 4, 13), true)
                                val instanceId_6_bytes = Hex.toHexStringv3(Hex.copyOfRange(namespaceMetadata, 13, 18), true)

                                Log.i(LOG_KEY, namespaceId_10_bytes)
                                return Beacon(
                                        namespaceId_10_bytes + ":" + instanceId_6_bytes,
                                        0,
                                        0,
                                        signalStrength,
                                        measuredPowerAt_0_m - 41,
                                        LocalDateTime.now(),
                                        device,
                                        PROTOCAL
                                )
//                                }
                            }
                        }
                    }
                }
            }

            return null
        }

        fun getMeasuredPower(signalStrenghth: Int, measuredPower: Int): Int {
            var found = false
            var measuredPower: Int = -62

            RSSI_TO_MEASURED_POWER.entries.forEach { entry ->
                if (!found && signalStrenghth > entry.key) {
                    measuredPower = entry.value
                    found = true
                }
            }

            Log.i(LOG_KEY, "strength " + signalStrenghth + " : " + measuredPower)
            return measuredPower
        }
    }
}
