//package com.duwamish.radio.transmitter.controller
//
//import android.content.Context
//import com.duwamish.radio.transmitter.data.Beacon
//
//class ScanState {
//
//    private var beacons = HashMap<String, Beacon>()
//
//    private fun scanThread(context: Context) = object : Runnable {
//        override fun run() {
//
//            val scanner = bluetoothAdapter.bluetoothLeScanner
//
//            val scan = object : ScanCallback() {
//                override fun onScanResult(callbackType: Int, result: ScanResult) {
//                    Log.i(LOG_KEY, "found BLEs result $callbackType")
//
//                    val ibeacon = IBeaconScanApi.scan(
//                            result.device,
//                            result.rssi,
//                            result.txPower,
//                            result.scanRecord.bytes
//                    )
//
//                    val eddystone = EddystoneScanApi.scan(
//                            result.device,
//                            result.rssi,
//                            result
//                    )
//
//                    if (ibeacon != null) {
//                        beacons.put(ibeacon.uuid, ibeacon)
//                    }
//
//                    if (eddystone != null) {
//                        beacons.put(eddystone.uuid, eddystone)
//                    }
//
//                    val beaconsViewAdaptor = ArrayAdapter<String>(
//                            context,
//                            android.R.layout.simple_list_item_1,
//                            beacons.map { b ->
//                                "UUID: " + b.key + "\n" +
//                                        "Major: " + b.value.major + "\n" +
//                                        "Minor: " + b.value.minor + "\n" +
//                                        "Rssi: " + b.value.rsStrengthIndicator + "\n" +
//                                        "Last Detected: " + b.value.lastDetected.toString() + "\n"
//                            }
//                    )
//
//                    beaconsView.adapter = beaconsViewAdaptor
//                }
//
//                override fun onBatchScanResults(results: List<ScanResult>) {
//                    Log.i(LOG_KEY, "scanning batch result $results")
//                    results.forEach { result -> onScanResult(0, result) }
//                }
//
//                override fun onScanFailed(errorCode: Int) {
//                    Log.i(LOG_KEY, "scanning failed with code SCAN_FAILED_$errorCode. " +
//                            "See https://developer.android.com/reference/android/bluetooth/le/ScanCallback.html for more")
//                    if (errorCode == 2) {
//                        val builder = AlertDialog.Builder(context)
//                        builder.setTitle("Error")
//                        builder.setMessage("Error scanning. Please reboot Bluetooth or device")
//                        builder.setPositiveButton(android.R.string.ok, null)
//                        builder.setOnDismissListener { }
//                        builder.show()
//                        //bluetoothAdapter.disable()
//                        //bluetoothAdapter.enable()
//                    }
//                }
//            }
//
//            if (isScanning) {
//                scanner.stopScan(scan)
//            } else {
//                scanner.startScan(scan)
//            }
//
//            isScanning = !isScanning
//
//            scanHandler.postDelayed(this, scan_interval_ms)
//        }
//    }
//}