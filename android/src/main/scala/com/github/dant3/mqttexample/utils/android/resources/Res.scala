package com.github.dant3.mqttexample.utils.android.resources

import android.content.Context

trait Res[T] {
  def apply(context:Context):T
}
