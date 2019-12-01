package com.duwamish.radio.transmitter.controller

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.duwamish.radio.transmitter.R
import com.duwamish.radio.transmitter.state.ApplicationState
import com.duwamish.radio.transmitter.view.BleListTab
import com.duwamish.radio.transmitter.view.BleRadarTab
import com.duwamish.radio.transmitter.view.BleTabView
import com.duwamish.radio.transmitter.view.UserInfoTab

class StandardBeaconCatcherController : AppCompatActivity() {

    private val LOG_KEY = this.javaClass.name;

    private var PERMISSION_REQUEST_COARSE_LOCATION = 1

    private lateinit var bleTabView: BleTabView
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById<ViewPager>(R.id.viewPager)
        tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        bleTabView = BleTabView(supportFragmentManager)

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

            setupBLE()
            renderTab()

        } else {
            setupBLE()
            renderTab()
        }
    }

    private fun setupBLE() {
        ApplicationState.bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        ApplicationState.bluetoothAdapter = ApplicationState.bluetoothManager.adapter
    }

    private fun renderTab() {
        bleTabView.addFragment(BleListTab(), "beacons")
        bleTabView.addFragment(BleRadarTab(), "proximity")
        bleTabView.addFragment(UserInfoTab(), "profile")

        viewPager.adapter = bleTabView
        tabLayout.setupWithViewPager(viewPager)
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
