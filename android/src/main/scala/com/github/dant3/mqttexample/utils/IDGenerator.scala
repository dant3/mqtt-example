package com.github.dant3.mqttexample.utils

import java.util.concurrent.atomic.AtomicLong

class IDGenerator {
  private val counter:AtomicLong = new AtomicLong(0)

  def apply():Long = counter.incrementAndGet()
}
