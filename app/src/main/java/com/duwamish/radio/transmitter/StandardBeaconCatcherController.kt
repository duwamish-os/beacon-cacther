package com.duwamish.radio.transmitter

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.ParcelUuid
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.ArrayAdapter
import com.duwamish.radio.transmitter.layouts.EddystoneScanApi
import com.duwamish.radio.transmitter.layouts.IBeaconScanApi
import java.util.*
import kotlin.collections.HashMap
import android.util.Base64

class StandardBeaconCatcherController : AppCompatActivity() {

    private val LOG_KEY = this.javaClass.name;

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private val scanHandler = Handler()
    private val scan_interval_ms = 5000L
    private var isScanning = false
    private var PERMISSION_REQUEST_COARSE_LOCATION = 1
    private lateinit var beaconsView: ListView

    private var beacons = HashMap<String, Beacon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        beaconsView = findViewById<ListView>(R.id.beacons)

        val alertDialog = AlertDialog.Builder(this)
        val locationGranted =
                this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && locationGranted) {
            alertDialog.setTitle("This app needs location access")
            alertDialog.setMessage("Please grant location access so this app can detect beacons.")
            alertDialog.setPositiveButton(android.R.string.ok, null)

            alertDialog.setOnDismissListener { _ ->
                requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        PERMISSION_REQUEST_COARSE_LOCATION
                )
            }

            alertDialog.show()
        } else {
            scanHandler.post(scanThread(this))
        }

        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
    }

    private fun scanThread(context: Context) = object : Runnable {
        override fun run() {

            val scanner = bluetoothAdapter.bluetoothLeScanner

            val scan = object : ScanCallback() {
                override fun onScanResult(callbackType: Int, result: ScanResult) {
                    Log.i(LOG_KEY, "found BLEs result $callbackType")

                    val beacon = IBeaconScanApi.scan(
                            result.device,
                            result.rssi,
                            result.scanRecord.bytes
                    )

                    val eddystone = EddystoneScanApi.scan(
                            result.device,
                            result.rssi,
                            result
                    )

                    if (beacon != null) {
                        beacons.put(beacon.uuid, beacon)
                    }

                    if (eddystone != null) {
                        beacons.put(eddystone.uuid, eddystone)
                    }

                    val beaconsViewAdaptor = ArrayAdapter<String>(
                            context,
                            android.R.layout.simple_list_item_1,
                            beacons.map { b ->
                                "UUID: " + b.key + "\n" +
                                        "Major: " + b.value.major + "\n" +
                                        "Minor: " + b.value.minor + "\n" +
                                        "Rssi: " + b.value.rssi + "\n" +
                                        "Last Detected: " + b.value.lastDetected.toString() + "\n"
                            }
                    )

                    beaconsView.adapter = beaconsViewAdaptor
                }

                override fun onBatchScanResults(results: List<ScanResult>) {
                    Log.i(LOG_KEY, "scanning batch result $results")
                    results.forEach { result -> onScanResult(0, result) }
                }

                override fun onScanFailed(errorCode: Int) {
                    Log.i(LOG_KEY, "scanning failed with code SCAN_FAILED_$errorCode. " +
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

            scanHandler.postDelayed(this, scan_interval_ms)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_COARSE_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_KEY, "coarse location permission granted")
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Functionality limited")
                    builder.setMessage("Since location access has not been granted, " +
                            "this app will not be able to discover beacons when in the background.")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setOnDismissListener { }
                    builder.show()
                }
                return
            }
        }
    }
}
