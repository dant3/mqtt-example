package com.github.dant3.mqttexample.utils.adapters

import com.github.dant3.mqttexample.utils.android.concurrent.{AsyncExecutor, GuiThreadExecutor}

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait AsyncAdapter[T] extends SeqAdapter[T] {
  private val empty:Seq[T] = Seq.empty
  private var currentContent:Option[Seq[T]] = None

  private lazy val contentLoading:Future[Seq[T]] = initiateContentLoading

  override def content(): Seq[T] = if (isLoading) {
    empty
  } else {
    currentContent.getOrElse(empty)
  }

  def isLoading = !contentLoading.isCompleted

  protected def initiateContentLoading:Future[Seq[T]] = {
    implicit val ctx = GuiThreadExecutor.context
    val loading = Future(loadContentInBackground)(AsyncExecutor.context)
    loading.onComplete {
      case Success(newData) =>
        currentContent = Some(newData)
        notifyDataSetChanged()
      case Failure(t) =>
        notifyDataSetChanged()
    }
    loading
  }

  protected def loadContentInBackground:Seq[T]
}
