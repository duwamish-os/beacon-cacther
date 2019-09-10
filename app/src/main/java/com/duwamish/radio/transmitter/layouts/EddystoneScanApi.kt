package com.duwamish.radio.transmitter.layouts

import android.bluetooth.BluetoothDevice
import android.util.Log
import com.duwamish.radio.transmitter.BeaconData
import com.duwamish.radio.transmitter.Hex
import java.time.LocalDateTime
import java.util.*

public class EddystoneScanApi {

    companion object {

        val LOG_KEY = this.javaClass.name

        fun scan(device: BluetoothDevice,
                 rssi: Int,
                 scanRecord: ByteArray): BeaconData? {

            Log.i(LOG_KEY, "validating eddystone")

            for (startByte in 0 until scanRecord.size) {
                if (scanRecord.size - startByte > 19) { // need at least 19 bytes for Eddystone-UID
                    // Check that this has the right pattern needed for this to be Eddystone-UID
                    if (Hex.isEddyV2(scanRecord, startByte)) {
                        // This is an Eddystone-UID beacon.
                        val namespaceIdentifierBytes = Arrays.copyOfRange(scanRecord, startByte + 4, startByte + 13)
                        val instanceIdentifierBytes = Arrays.copyOfRange(scanRecord, startByte + 14, startByte + 19)

                        Log.i(LOG_KEY, "Eddystone found")
                        return BeaconData(
                                instanceIdentifierBytes.toString(),
                                0,
                                0,
                                rssi,
                                LocalDateTime.now()
                        );
                    } else {
                        Log.i(LOG_KEY, "is not eddy")
                    }
                }

            }

            return null
        }
    }
}
