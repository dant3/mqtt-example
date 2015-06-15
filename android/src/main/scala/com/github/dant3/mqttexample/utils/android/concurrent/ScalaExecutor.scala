package com.github.dant3.mqttexample.utils.android.concurrent

import java.util.concurrent.Executor

trait ScalaExecutor { self:Executor ⇒
  def execute(block: ⇒ Any):Unit = self.execute(new Runnable {
    override def run(): Unit = block
  })
}
