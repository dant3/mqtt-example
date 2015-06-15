package com.github.dant3.mqttexample.utils

import _root_.android.app.Activity
import _root_.android.content.Context
import _root_.android.view.{KeyEvent, View}
import _root_.android.view.inputmethod.{EditorInfo, InputMethodManager}
import _root_.android.widget.TextView

object Keyboard {
  def hide(activity:Activity) = {
    val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE).asInstanceOf[InputMethodManager]
    val currentFocusView = activity.getCurrentFocus
    if (currentFocusView != null && inputMethodManager != null) {
      inputMethodManager.hideSoftInputFromWindow(
        currentFocusView.getWindowToken, 0 /*InputMethodManager.HIDE_NOT_ALWAYS*/)
    }
  }

  def show(activity:Activity, viewToFocus:View) = {
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE).asInstanceOf[InputMethodManager]
    if (inputMethodManager != null) {
      inputMethodManager.showSoftInput(viewToFocus, 0);
    }
  }

  def showLater(activity:Activity, viewToFocus:View) = {
    viewToFocus.post(new Runnable() {
      override def run() = show(activity, viewToFocus)
    })
  }

  trait ActionListener extends TextView.OnEditorActionListener {
    val actions = Set(
      EditorInfo.IME_ACTION_UNSPECIFIED,
      EditorInfo.IME_ACTION_NONE,
      EditorInfo.IME_ACTION_GO,
      EditorInfo.IME_ACTION_SEARCH,
      EditorInfo.IME_ACTION_SEND,
      EditorInfo.IME_ACTION_NEXT,
      EditorInfo.IME_ACTION_DONE
    )

    override def onEditorAction(v:TextView, actionID:Int, event:KeyEvent):Boolean =
      actions.contains(actionID) && onAction(v, actionID)

    def onAction(textView:TextView, actionID:Int):Boolean
  }
}
