package com.github.dant3.mqttexample.utils

import _root_.android.content.Context

private[utils] class UnitConversion(val ext: Double)(implicit context: Context) {
  @inline private def metrics = context.getResources.getDisplayMetrics

  @inline def px    : Int = ext.toInt
  @inline def dip   : Int = (ext * metrics.density).toInt
  @inline def sp    : Int = (ext * metrics.scaledDensity).toInt
  @inline def px2dip: Double = ext / metrics.density
  @inline def px2sp : Double = ext / metrics.scaledDensity
}

object UnitConversion {
  @inline implicit def Double2unitConversion(ext: Double)(implicit context: Context): UnitConversion =
    new UnitConversion(ext)(context)
  @inline implicit def Long2unitConversion  (ext: Long)  (implicit context: Context): UnitConversion =
    new UnitConversion(ext)(context)
  @inline implicit def Int2unitConversion   (ext: Int)   (implicit context: Context): UnitConversion =
    new UnitConversion(ext)(context)
}