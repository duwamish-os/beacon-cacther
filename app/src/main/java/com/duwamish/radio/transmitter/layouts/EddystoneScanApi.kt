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

public class EddystoneScanApi {

    companion object {

        private val PROTOCAL = "eddystone"

        private val LOG_KEY = this.javaClass.name
        private val EDDYSTONE_UUID = ParcelUuid.fromString("0000feaa-0000-1000-8000-00805f9b34fb")

        fun scan(device: BluetoothDevice,
                 rssi: Int,
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
                    Log.i(LOG_KEY, "Eddystone found: ${serviceUuids}")
                    val namespaceMetadata: ByteArray? = serviceData.get(EDDYSTONE_UUID)

                    if (namespaceMetadata != null) {
                        when (namespaceMetadata.get(0).toInt()) {
                            0 -> {
                                val ns: String = "urn:feaa:uid:" + Base64.encodeToString(
                                        Arrays.copyOfRange(namespaceMetadata, 2, 18), 2
                                )
                                val namespaceIndex = ns.lastIndexOf(58.toChar())
                                if (namespaceIndex != -1) {
                                    ns.substring(0, namespaceIndex + 1)
                                    val ns1 = ns.substring(namespaceIndex + 1).split(";")[0]
                                    val namespaceId = Hex.toHexString(Base64.decode(ns1, 2), 0, 10)

                                    return Beacon(
                                            namespaceId,
                                            0,
                                            0,
                                            rssi,
                                            transmittedPower,
                                            Math.pow(10.0, (transmittedPower.toDouble() - rssi) / (10 * 2)),
                                            LocalDateTime.now(),
                                            device,
                                            PROTOCAL
                                    )
                                }
                            }
                        }
                    }
                }
            }

            return null
        }
    }
}
