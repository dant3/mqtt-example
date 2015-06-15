package com.github.dant3.mqttexample.utils.focused

import scala.collection.mutable

class FocusedMutableSet[A] extends FocusedMutable[mutable.Set[A], Set[A]] {
  override protected def initialMutableData = mutable.Set.empty[A]
  override protected def immutableCopyOf(mutableData: mutable.Set[A]): Set[A] = mutableData.toSet
}
