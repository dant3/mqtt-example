package com.github.dant3.mqttexample.utils.adapters

import android.os.AsyncTask
import com.github.dant3.mqttexample.utils.android.concurrent.GuiThreadExecutor

trait FilteredAdapter[T <: AnyRef] extends SeqAdapter[T] {
  private var _constraint:Option[CharSequence] = None
  private var _filteredContent:Option[Seq[T]] = None

  override def getCount = _filteredContent match {
    case Some(list) => list.size
    case None => super.getCount
  }

  override def getItem(position:Int):T = _filteredContent match {
    case Some(list) => list(position)
    case None => super.getItem(position)
  }

  def resetFilter() = {
    _constraint = None
    _filteredContent = None
    notifyDataSetChanged()
  }

  def filter(constraint:CharSequence) = constraint match {
    case null => resetFilter()
    case _ =>
      val contentToFilter = content()
      AsyncTask.THREAD_POOL_EXECUTOR.execute {
        val filteredContent = filterContent(constraint, contentToFilter)
        GuiThreadExecutor.execute {
          _filteredContent = Option(filteredContent)
          _constraint = Some(constraint)
          notifyDataSetChanged()
        }
      }
  }


  private def filterContent(constraint:CharSequence, content:Seq[T]): Seq[T] = for {
    item <- content
    if itemFilter(item, constraint)
  } yield item

  protected def itemFilter:(T,CharSequence) => Boolean

  private implicit def toRunnable(f: => Any):Runnable = new Runnable {
    override def run(): Unit = f
  }
}
