package com.github.dant3.mqttexample.utils.android.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

trait BoundService[T <: BoundService[T]] extends Service { this:T ⇒
  private val binder = BoundService.binder(this)

  override def onBind(intent: Intent): IBinder = binder
  override def onStartCommand(intent:Intent, flags:Int, startId:Int):Int = Service.START_NOT_STICKY
}

object BoundService {
  private def binder[T <: BoundService[T]](service:T) = new Binder[T](service)

  class Binder[T <: BoundService[T]](service:T) extends android.os.Binder {
    def getService:T = service
  }
}
