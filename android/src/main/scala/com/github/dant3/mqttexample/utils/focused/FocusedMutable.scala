package com.github.dant3.mqttexample.utils.focused

trait FocusedMutable[Mutable,Immutable] {
  private val mutableDataMonitor = new AnyRef
  private val mutableData:Mutable = initialMutableData
  private var immutableData:Immutable = immutableCopyOf(initialMutableData)

  final def read:Immutable = immutableData
  def modify[B](modifier: Mutable ⇒ B) = mutableDataMonitor.synchronized {
    val mutable = mutableData
    val modificationResult = modifier.apply(mutable)
    immutableData = immutableCopyOf(mutable)
    modificationResult
  }


  protected def initialMutableData:Mutable
  protected def immutableCopyOf(mutableData:Mutable):Immutable
}
