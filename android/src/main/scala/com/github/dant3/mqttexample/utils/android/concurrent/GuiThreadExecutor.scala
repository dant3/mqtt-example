package com.github.dant3.mqttexample.utils.android.concurrent

import android.os.Looper

object GuiThreadExecutor extends LooperExecutor(Looper.getMainLooper) {
  def executeLater(task: ⇒ Any) = handler.post(new Runnable() { override def run() = task })
}
