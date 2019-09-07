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


class MainActivity : AppCompatActivity(), BeaconConsumer, RangeNotifier {

    private lateinit var beaconManager: BeaconManager;

    override fun onBeaconServiceConnect() {

        println("onBeaconServiceConnect")
        beaconManager.removeAllMonitorNotifiers();

        var a = object : MonitorNotifier {
            override fun didDetermineStateForRegion(p0: Int, p1: Region?) {
                println("didDetermineStateForRegion")
            }

            override fun didEnterRegion(p0: Region?) {
                println("didEnterRegion ${p0}")
                println("didEnterRegion ${p0?.bluetoothAddress}")
            }

            override fun didExitRegion(p0: Region?) {
                println("didExitRegion ${p0}")
            }

        }
        beaconManager.addMonitorNotifier(a)
    }

    override fun didRangeBeaconsInRegion(p0: MutableCollection<Beacon>?, p1: Region?) {
        println("didRangeBeaconsInRegion $p0")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        beaconManager = BeaconManager.getInstanceForApplication(this)

        beaconManager.getBeaconParsers()
            .add(BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT))

        beaconManager.bind(this)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            Log.e(this.javaClass.name, "bluetooth not supported")
        } else {
            if (mBluetoothAdapter.isEnabled) {
               Log.i(this.javaClass.name, "bluetooth is enabled")
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
