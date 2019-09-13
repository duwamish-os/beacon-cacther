package com.duwamish.radio.transmitter

import java.time.LocalDateTime

data class Beacon(val uuid: String,
                  val major: Int,
                  val minor: Int,
                  val rssi: Int,
                  val lastDetected: LocalDateTime
)
