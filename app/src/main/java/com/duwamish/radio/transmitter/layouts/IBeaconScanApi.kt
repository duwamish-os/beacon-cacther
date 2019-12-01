package com.duwamish.radio.transmitter.layouts

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
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
                 scannedBleRecord: ScanResult): Beacon? {

            val scanBleRecord = scannedBleRecord.scanRecord.bytes

            var advertisingDataStartByte = 2

            var iBeaconPatternFound = false
            while (advertisingDataStartByte <= 5) {
                if (scanBleRecord[advertisingDataStartByte + 2].toInt() and 0xff == 0x02 && //Identifies an iBeacon
                    scanBleRecord[advertisingDataStartByte + 3].toInt() and 0xff == 0x15) { //Identifies correct data length

                    iBeaconPatternFound = true
                    break
                }
                advertisingDataStartByte++
            }

            if (iBeaconPatternFound) {
                Log.i(LOG_TAG, "${advertisingDataStartByte} " +
                        "validated ${scanBleRecord.size} bytes Ibeacon BLE for ${scannedBleRecord.scanRecord}")
                //Convert to hex String
                val uuidBytes_16_bytes = ByteArray(16)
                System.arraycopy(scanBleRecord,
                        advertisingDataStartByte + 4,
                        uuidBytes_16_bytes, 0, 16
                )

                val hexString = Hex.bytesToHex(uuidBytes_16_bytes)

                //UUID detection
                val uuid_32_bytes = hexString.substring(0, 8) + "-" +
                        hexString.substring(8, 12) + "-" +
                        hexString.substring(12, 16) + "-" +
                        hexString.substring(16, 20) + "-" +
                        hexString.substring(20, 32)

                // major
                val majorGroup = Hex.extract(
                        scanBleRecord,
                        advertisingDataStartByte + 20, //25
                        advertisingDataStartByte  + 21 //26
                )

                // minor
                val minorId = Hex.extract(
                        scanBleRecord,
                        advertisingDataStartByte + 22, //27
                        advertisingDataStartByte + 23 //28
                )

                val tx = scanBleRecord[advertisingDataStartByte + 24]; //29

                Log.i(LOG_TAG, "UUID: $uuid_32_bytes, major: $majorGroup, minor: $minorId, " +
                        "RSSI@1m: $tx, name: ${device.name}")

                return Beacon(
                        uuid_32_bytes,
                        majorGroup,
                        minorId,
                        rsStrengthIndicator,
                        if(transmitPower == 127) tx.toInt() else transmitPower,
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
