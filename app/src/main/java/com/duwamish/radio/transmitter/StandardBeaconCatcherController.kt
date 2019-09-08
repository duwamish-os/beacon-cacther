package com.duwamish.radio.transmitter

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*

class StandardBeaconCatcherController : AppCompatActivity() {

    private val LOG_KEY = this.javaClass.name;

    private lateinit var btManager: BluetoothManager
    private lateinit var btAdapter: BluetoothAdapter
    private val scanHandler = Handler()
    private val scan_interval_ms = 5000L
    private var isScanning = false
    private var PERMISSION_REQUEST_COARSE_LOCATION = 1
    private lateinit var editText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        editText = findViewById<TextView>(R.id.beacon_name)

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
            scanHandler.post(scanRunnable)
        }

        btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = btManager.adapter
    }

    private val scanRunnable = object : Runnable {
        override fun run() {

            if (isScanning) {
                btAdapter.stopLeScan { device, rssi, scanRecord ->
                    IBeaconScanApi.ibeacon(
                        device,
                        rssi,
                        scanRecord
                    )
                }
            } else {
                btAdapter.startLeScan { device, rssi, scanRecord ->
                    val beacon = IBeaconScanApi.ibeacon(
                        device,
                        rssi,
                        scanRecord
                    )
                    if(beacon != "no ibeacons found") {
                        editText.setText(beacon)
                    }
                }
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
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_COARSE_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_KEY, "coarse location permission granted")
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Functionality limited")
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setOnDismissListener { }
                    builder.show()
                }
                return
            }
        }
    }
}
