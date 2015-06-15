package com.github.dant3.mqttexample.utils.android

import android.app.{Dialog, ProgressDialog}
import android.os.Bundle
import android.support.v4.app.{DialogFragment, FragmentActivity}
import com.github.dant3.mqttexample.utils.BundleUtils
import com.github.dant3.mqttexample.utils.android.UiImplicits._
import com.github.dant3.mqttexample.utils.android.concurrent.{FutureCache, GuiThreadExecutor}

import scala.concurrent.Future
import scala.util.Try

class ProgressDialogFragment extends DialogFragment {
  private lazy val arguments:ProgressDialogFragment.Args = new ProgressDialogFragment.Args(getArguments)

  override def onCreateDialog(savedInstanceState: Bundle): Dialog = {
    val dialog = new ProgressDialog(getActivity)
    dialog.setTitle(arguments.title)
    dialog.setMessage(getString(arguments.text))
    dialog.setIndeterminate(true)
    dialog.setCancelable(false)
    arguments.process match {
      case Some(future) ⇒
        future.onCompleteUi { _ ⇒ tryToDismiss }
      case None ⇒
        GuiThreadExecutor.executeLater { tryToDismiss }
    }
    dialog
  }

  private def tryToDismiss = Try{ dismissAllowingStateLoss() }
}

object ProgressDialogFragment {
  val tag = "ProgressDialogFragment"
  private val processCache:FutureCache[Any] = new FutureCache[Any]

  def apply(title:Int, text:Int, process:Future[Any]) = {
    val newInstance = new ProgressDialogFragment
    val args = new Args(title, text, processCache.put(process))
    newInstance.setArguments(args.toBundle)
    newInstance
  }

  def show(activity:FragmentActivity, title:Int, text:Int, process:Future[Any]) = {
    val fragmentManager = activity.getSupportFragmentManager
    val transaction = fragmentManager.beginTransaction
    val previousDialog = fragmentManager.findFragmentByTag(tag)
    if (previousDialog != null) {
      transaction.remove(previousDialog)
    }
    transaction.addToBackStack(null)

    // Create and show the dialog.
    val dialog = apply(title, text, process)
    dialog.show(transaction, tag)
  }


  class Args(val title:Int, val text:Int, processID:Long) {
    def this(bundle:Bundle) = this(bundle.getInt("title"), bundle.getInt("text"), bundle.getLong("process_id"))
    def process:Option[Future[_]] = processCache.get(processID)
    def toBundle: Bundle = BundleUtils.bundle (
      "title" → title,
      "text" → text,
      "process_id" → processID
    )
  }
}