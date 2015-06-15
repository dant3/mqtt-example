package com.github.dant3.mqttexample.utils.focused

import scala.collection.mutable

class FocusedMutableMap[A,B] extends FocusedMutable[mutable.Map[A,B],Map[A,B]] {
  override protected def initialMutableData: mutable.Map[A, B] = mutable.Map()
  override protected def immutableCopyOf(mutableData: mutable.Map[A, B]): Map[A, B] = mutableData.toMap
}
