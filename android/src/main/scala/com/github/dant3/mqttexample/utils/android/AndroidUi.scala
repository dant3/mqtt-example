package com.github.dant3.mqttexample.utils.android

import android.app.Activity
import android.view.View

trait AndroidUi { self:Activity ⇒
  def findView[T <: View](id:Int):T = self.findViewById(id).asInstanceOf[T]
}
