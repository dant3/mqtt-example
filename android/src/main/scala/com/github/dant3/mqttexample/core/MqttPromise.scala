package com.github.dant3.mqttexample.core

import org.fusesource.mqtt.client.Callback

import scala.concurrent.Promise

class MqttPromise[T] extends Callback[T] {
  private val promise = Promise[T]()
  def future = promise.future

  override def onFailure(value: Throwable): Unit = promise.failure(value)
  override def onSuccess(value: T): Unit = promise.success(value)
}