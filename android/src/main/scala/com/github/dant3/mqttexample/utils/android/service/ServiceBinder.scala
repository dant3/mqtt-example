package com.github.dant3.mqttexample.utils.android.service

import android.app.Service
import android.content.{Intent, Context}

class ServiceBinder[T <: Service](implicit manifest:Manifest[T]) {
  def bind(context:Context):ServiceBinding[T] = {
    bind(context, new ServiceBinding[T](context)(manifest))
  }

  def bind(context:Context, connection:ServiceBinding[T]):ServiceBinding[T] = {
    val startIntent = new Intent(context.getApplicationContext, manifest.runtimeClass)
    context.startService(startIntent)
    context.bindService(startIntent, connection, Context.BIND_AUTO_CREATE)
    connection
  }
}
