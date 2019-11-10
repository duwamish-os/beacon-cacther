package com.duwamish.radio.transmitter.controller

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.duwamish.radio.transmitter.data.Beacon
import com.duwamish.radio.transmitter.layouts.EddystoneScanApi
import com.duwamish.radio.transmitter.layouts.IBeaconScanApi

class ScanState {

    companion object {
        private var beacons = HashMap<String, Beacon>()

        private val scanIntervalMs = 10000L
        private var isScanning = false

        private val LOG_KEY = this.javaClass.name;

        public fun scanThread(scanHandler: Handler,
                              beaconsView: ListView,
                              context: Context,
                              bluetoothAdapter: BluetoothAdapter) = object : Runnable {
            override fun run() {

                val scanner = bluetoothAdapter.bluetoothLeScanner

                val scan = object : ScanCallback() {
                    override fun onScanResult(callbackType: Int, scanResult: ScanResult) {
                        Log.i(LOG_KEY, "found BLEs result $scanResult")

                        val ibeacon = IBeaconScanApi.scan(
                                scanResult.device,
                                scanResult.rssi,
                                scanResult.txPower,
                                scanResult.scanRecord.bytes
                        )

                        val eddystone = EddystoneScanApi.scan(
                                scanResult.device,
                                scanResult.rssi,
                                scanResult.txPower,
                                scanResult
                        )

                        if (ibeacon != null) {
                            beacons.put(ibeacon.uuid, ibeacon)
                        }

                        if (eddystone != null) {
                            beacons.put(eddystone.uuid, eddystone)
                        }

                        val beaconsViewAdaptor = ArrayAdapter<String>(
                                context,
                                android.R.layout.simple_list_item_1,
                                beacons.map { b ->
                                    "UUID: " + b.key + "\n" +
                                            "Protocol: " + b.value.protocol + "\n" +
                                            "Major: " + b.value.major + " / " + "Minor: " + b.value.minor + "\n" +
                                            "1m Transmitted Power: " + b.value.measuredPower + " dBMW \n" +
                                            "Signal Strength: " + b.value.rsStrengthIndicator + " dBMW \n" +
                                            "Estimated Distance: " + b.value.estimatedDistance() + " m \n" +
                                            "Last Detected: " + b.value.lastDetected.toString() + "\n"
                                }
                        )

                        beaconsView.adapter = beaconsViewAdaptor
                    }

                    override fun onBatchScanResults(results: List<ScanResult>) {
                        Log.i(LOG_KEY, "scanning batch result for ${results.size} beacons")
                        results.forEach { result -> onScanResult(0, result) }
                    }

                    override fun onScanFailed(errorCode: Int) {
                        Log.i(LOG_KEY, "scanning failed with code SCAN_FAILED_$errorCode. \n" +
                                "See https://developer.android.com/reference/android/bluetooth/le/ScanCallback.html for more")
                        if (errorCode == 2) {
                            val builder = AlertDialog.Builder(context)
                            builder.setTitle("Error")
                            builder.setMessage("Error scanning. Please reboot Bluetooth or device")
                            builder.setPositiveButton(android.R.string.ok, null)
                            builder.setOnDismissListener { }
                            builder.show()
                            //bluetoothAdapter.disable()
                            //bluetoothAdapter.enable()
                        }
                    }
                }

                if (isScanning) {
                    scanner.stopScan(scan)
                } else {
                    scanner.startScan(scan)
                }

                isScanning = !isScanning

                scanHandler.postDelayed(this, scanIntervalMs)
            }
        }
    }

}
