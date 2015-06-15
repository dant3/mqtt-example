package com.github.dant3.mqttexample.core

import com.github.dant3.mqttexample.utils.focused.FocusedMutableSet
import rx.lang.scala.Subscriber

class ProxySubscriber[T] extends Subscriber[T] {
  val childs = new FocusedMutableSet[Subscriber[T]]

  override def onNext(value:T) = foreachChild(_.onNext(value))
  override def onError(error: Throwable): Unit = foreachChild(_.onError(error))
  override def onCompleted(): Unit = foreachChild(_.onCompleted())

  private def foreachChild(fn:Subscriber[T] ⇒ Any) = {
    childs.modify(_.retain(!_.isUnsubscribed))
    for {
      child ← childs.read
      if !child.isUnsubscribed
    } fn(child)
  }
}
