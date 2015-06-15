package com.github.dant3.mqttexample.utils.android

import android.view.View
import com.github.dant3.mqttexample.utils.android.concurrent.GuiThreadExecutor

import scala.concurrent.Future
import scala.language.implicitConversions
import scala.util.Try

object UiImplicits {
  implicit def onClickListener(fn:(View) ⇒ Unit):View.OnClickListener = new View.OnClickListener {
    override def onClick(v: View): Unit = fn(v)
  }
  implicit def onClickListener(fn: ⇒ Unit):View.OnClickListener = new View.OnClickListener {
    override def onClick(v: View): Unit = fn
  }

  implicit class UiFuture[T](future:Future[T]) {
    implicit private val executionContext = GuiThreadExecutor.context
    def onSuccessUi[U](pf:PartialFunction[T,U]) = future.onSuccess(pf)
    def onFailureUi[U](pf:PartialFunction[Throwable,U]) = future.onFailure(pf)
    def onCompleteUi[U](fn:Try[T] ⇒ U) = future.onComplete(fn)
    def mapUi[U](f: T ⇒ U) = future.map(f)
    def flatMapUi[U](f: T ⇒ Future[U]) = future.flatMap(f)
  }
}
