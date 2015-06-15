package com.github.dant3.mqttexample.utils.focused

import rx.lang.scala.{RxVal, RxVar}

trait RxFocusedMutable[Mutable,Immutable] extends FocusedMutable[Mutable, RxVal[Immutable]] {
  private val rxCopy:RxVar[Immutable] = RxVar(makeCopyOf(initialMutableData))

  override final protected def immutableCopyOf(mutableData: Mutable): RxVal[Immutable] = {
    rxCopy := makeCopyOf(mutableData)
    rxCopy
  }
  protected def makeCopyOf(mutableData:Mutable):Immutable
}
