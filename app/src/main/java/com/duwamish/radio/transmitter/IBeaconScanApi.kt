package com.duwamish.radio.transmitter

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothAdapter
import android.util.Log


public class IBeaconScanApi {

    companion object {

        private val LOG_TAG = "IBeaconScanApi"

        fun ibeacon(device: BluetoothDevice, rssi: Int, scanRecord: ByteArray): String {
            var startByte = 2
            var patternFound = false
            while (startByte <= 5) {
                if (scanRecord[startByte + 2].toInt() and 0xff == 0x02 && //Identifies an iBeacon
                    scanRecord[startByte + 3].toInt() and 0xff == 0x15
                ) { //Identifies correct data length
                    patternFound = true
                    break
                }
                startByte++
            }

            if (patternFound) {
                //Convert to hex String
                val uuidBytes = ByteArray(16)
                System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16)
                val hexString = Hex.bytesToHex(uuidBytes)

                //UUID detection
                val uuid = hexString.substring(0, 8) + "-" +
                        hexString.substring(8, 12) + "-" +
                        hexString.substring(12, 16) + "-" +
                        hexString.substring(16, 20) + "-" +
                        hexString.substring(20, 32)

                // major
                val major = Hex.major(scanRecord, startByte)

                // minor
                val minor = Hex.minor(scanRecord, startByte)

                Log.i(LOG_TAG, "UUID: $uuid, major: $major, minor: $minor, RSSI: $rssi, name: ${device.name}")

                return uuid
            } else {
                return ""
            }
        }

    }

}
