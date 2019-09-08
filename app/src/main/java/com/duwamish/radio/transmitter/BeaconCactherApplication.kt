package com.duwamish.radio.transmitter

import android.app.Application
import android.util.Log
import org.altbeacon.beacon.Region
import org.altbeacon.beacon.startup.BootstrapNotifier
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.startup.RegionBootstrap
import org.altbeacon.beacon.powersave.BackgroundPowerSaver

public class BeaconCactherApplication: Application(), BootstrapNotifier {

    private val LOG_KEY = this.javaClass.name

    private lateinit var regionBootstrap: RegionBootstrap
    private lateinit var backgroundPowerSaver: BackgroundPowerSaver

    override fun onCreate() {
        super.onCreate()

        Log.i(LOG_KEY, "boostrap notifier")

        val beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this)
        val region = Region("backgroundRegion", null, null, null)
        regionBootstrap = RegionBootstrap(this, region)
        backgroundPowerSaver = BackgroundPowerSaver(this);
    }

    override fun didDetermineStateForRegion(p0: Int, p1: Region?) {
        Log.i(LOG_KEY,"didDetermineStateForRegion")
    }

    override fun didEnterRegion(p0: Region?) {
        Log.i(LOG_KEY,"didEnterRegion ${p0}")
        Log.i(LOG_KEY,"didEnterRegion ${p0?.bluetoothAddress}")
    }

    override fun didExitRegion(p0: Region?) {
        Log.i(LOG_KEY,"didExitRegion ${p0}")
    }

}