package com.github.dant3.mqttexample.utils.android.service

import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicReference

import android.app.Service
import android.content.{Context, ComponentName, ServiceConnection}
import android.os.IBinder
import com.github.dant3.mqttexample.utils.Logger

import scala.collection.JavaConversions

class ServiceBinding[T <: Service](boundContext:Context)(implicit manifest:Manifest[T]) extends ServiceConnection with Logger {
  log.debug("Connection created...")

  private val binding = new AtomicReference[T]()
  private val listeners = JavaConversions.asScalaSet(new CopyOnWriteArraySet[ServiceBinding.Listener[T]]())


  def unbind() = getOption match {
    case Some(service) ⇒ boundContext.unbindService(this)
    case None ⇒ /* do nothing - we're not binded */
  }



  def addListener(listener: ServiceBinding.Listener[T]) = {
    listeners.add(listener)
    for (binding ← getOption) listener.onServiceConnected(binding)
    this
  }

  def removeListener(listener: ServiceBinding.Listener[T]) = {
    listeners.remove(listener)
    this
  }

  override def onServiceConnected(name: ComponentName, serviceBinder: IBinder) = {
    serviceBinder match {
      case serviceBinding: BoundService.Binder[T] ⇒
        log.debug(s"Connection with service ${manifest.runtimeClass.getName} established")
        val service = serviceBinding.getService
        binding.set(service)
        notifyServiceConnected(service)
      case _ ⇒
        log.warn("Somehow we got binder which is not boundService binder!")
    }
  }

  override def onServiceDisconnected(name: ComponentName) {
    notifyServiceDisconnected(binding.getAndSet(null.asInstanceOf[T]))
  }

  def getOption:Option[T] = Option(getBinding)
  def getBinding:T = binding.get()

  private def notifyServiceConnected(service: T) = {
    for (listener ← listeners) listener.onServiceConnected(service)
  }


  private def notifyServiceDisconnected(service: T) = {
    for (listener ← listeners) listener.onServiceDisconnected(service)
  }
}

object ServiceBinding {
  trait Listener[-T] {
    def onServiceConnected(service:T)
    def onServiceDisconnected(service:T)
  }
}