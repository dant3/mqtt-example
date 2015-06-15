package com.github.dant3.mqttexample.utils.adapters

import rx.android.schedulers.AndroidSchedulers
import rx.lang.scala.Observable
import rx.lang.scala.JavaConversions.javaSchedulerToScalaScheduler

abstract class RxSeqAdapter[T](dataSet:Observable[Seq[T]]) extends SeqAdapter[T] {
  private var currentData:Option[Seq[T]] = None

  dataSet.observeOn(AndroidSchedulers.mainThread()).subscribe { roster ⇒
    currentData = Option(roster)
    notifyDataSetChanged()
  }

  override def content(): Seq[T] = currentData getOrElse Seq.empty
}
