package com.duwamish.radio.transmitter.layouts

import android.bluetooth.BluetoothDevice
import android.util.Log
import com.duwamish.radio.transmitter.data.Beacon
import com.duwamish.radio.transmitter.Hex
import java.time.LocalDateTime

public class IBeaconScanApi {

    companion object {

        private val PROTOCAL = "Ibeacon"

        private val LOG_TAG = "IBeaconScanApi"

        fun scan(device: BluetoothDevice,
                 rsStrengthIndicator: Int,
                 transmitPower: Int,
                 scanBleRecord: ByteArray): Beacon? {
            Log.i(LOG_TAG, "validating if Ibeacon BLE for ${scanBleRecord}")

            var startByte = 2
            var iBeaconPatternFound = false
            while (startByte <= 5) {
                if (scanBleRecord[startByte + 2].toInt() and 0xff == 0x02 && //Identifies an iBeacon
                    scanBleRecord[startByte + 3].toInt() and 0xff == 0x15) { //Identifies correct data length

                    iBeaconPatternFound = true
                    break
                }
                startByte++
            }

            if (iBeaconPatternFound) {
                //Convert to hex String
                val uuidBytes = ByteArray(16)
                System.arraycopy(scanBleRecord, startByte + 4, uuidBytes, 0, 16)

                val hexString = Hex.bytesToHex(uuidBytes)

                //UUID detection
                val uuid = hexString.substring(0, 8) + "-" +
                        hexString.substring(8, 12) + "-" +
                        hexString.substring(12, 16) + "-" +
                        hexString.substring(16, 20) + "-" +
                        hexString.substring(20, 32)

                // major
                val major = Hex.major(scanBleRecord, startByte)

                // minor
                val minor = Hex.minor(scanBleRecord, startByte)

                Log.i(LOG_TAG, "UUID: $uuid, major: $major, minor: $minor, RSSI: $rsStrengthIndicator, name: ${device.name}")

                return Beacon(
                        uuid,
                        major,
                        minor,
                        rsStrengthIndicator,
                        transmitPower,
                        LocalDateTime.now(),
                        device,
                        PROTOCAL
                )
            } else {
                return null
            }
        }

    }

}
