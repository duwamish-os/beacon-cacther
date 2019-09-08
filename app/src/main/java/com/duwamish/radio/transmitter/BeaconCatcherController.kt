package com.duwamish.radio.transmitter

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import org.altbeacon.beacon.*
import android.bluetooth.BluetoothAdapter
import android.util.Log


class BeaconCatcherController : AppCompatActivity(), BeaconConsumer {

    private val LOG_KEY = this.javaClass.name;
    private val I_BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"

    private lateinit var beaconManager: BeaconManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        beaconManager = BeaconManager.getInstanceForApplication(this)

        beaconManager.beaconParsers.clear()
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(I_BEACON_LAYOUT))
//        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT))
        beaconManager.backgroundScanPeriod = 1000

        beaconManager.beaconParsers.forEach { p ->
            Log.i(LOG_KEY, "registered beacon layouts: " + p.layout)
        }

        beaconManager.bind(this)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        checkBluetooth()
    }

    override fun onBeaconServiceConnect() {

        Log.i(LOG_KEY, "setup beacon")

        beaconManager.removeAllMonitorNotifiers()
//        beaconManager.removeAllRangeNotifiers()

        val beaconMonitor = object : MonitorNotifier {
            override fun didDetermineStateForRegion(p0: Int, p1: Region?) {
                Log.i(LOG_KEY, "didDetermineStateForRegion " + p0)
                Log.i(LOG_KEY, "didDetermineStateForRegion " + p1)
            }

            override fun didEnterRegion(p0: Region?) {
                Log.i(LOG_KEY, "didEnterRegion ${p0}")
                Log.i(LOG_KEY, "didEnterRegion ${p0?.bluetoothAddress}")
            }

            override fun didExitRegion(p0: Region?) {
                Log.i(LOG_KEY, "didExitRegion ${p0}")
            }

        }

        beaconManager.addMonitorNotifier(beaconMonitor)
        beaconManager.startMonitoringBeaconsInRegion(Region("beacon-cacther", null, null, null))

//
//        val beaconRangeNotf = object : RangeNotifier {
//            override fun didRangeBeaconsInRegion(beacons: MutableCollection<Beacon>, p1: Region?) {
//                if(beacons.isNotEmpty()) {
//                    val b = beacons.first()
//                    Log.i(LOG_KEY, b.bluetoothAddress)
//                }
//            }
//        }
//
//        beaconManager.startRangingBeaconsInRegion(Region("queenanne", null, null, null))
//        beaconManager.addRangeNotifier(beaconRangeNotf)
//        beaconManager.startRangingBeaconsInRegion(Region("queenanne", null, null, null))
//        beaconManager.addRangeNotifier(beaconRangeNotf)
    }

    private fun checkBluetooth() {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            Log.e(LOG_KEY, "bluetooth not supported")
        } else {
            if (mBluetoothAdapter.isEnabled) {
                Log.i(LOG_KEY, "bluetooth is enabled")
            }
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
}
