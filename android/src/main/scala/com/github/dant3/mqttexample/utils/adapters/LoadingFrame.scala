package com.github.dant3.mqttexample.utils.adapters

import android.content.Context
import android.database.DataSetObserver
import android.view.ViewGroup.LayoutParams
import android.view.{Gravity, View, ViewGroup}
import android.widget.{FrameLayout, ListView, ProgressBar}

abstract class LoadingFrame(context:Context) extends FrameLayout(context) {
  val dataView:ListView = new ListView(context)
  val loadingView:ProgressBar = new ProgressBar(context)

  loadingView.setIndeterminate(true)
  
  addView(dataView, matchParent)
  addView(loadingView, centerContent)

  def setAdapter(adapter: AsyncAdapter[_]) = {
    dataView.setAdapter(adapter)
    adapter.registerDataSetObserver(new DataSetObserver {
      override def onChanged() = {
        setLoadingVisible(adapter.isLoading)
      }
    })
    setLoadingVisible(adapter.isLoading)
  }

  private def setLoadingVisible(loadingVisible:Boolean) = {
    loadingView.setVisibility(visibility(loadingVisible))
    dataView.setVisibility(visibility(!loadingVisible))
  }

  private def visibility(visible:Boolean) = visible match {
    case true => View.VISIBLE
    case false => View.GONE
  }

  private def matchParent = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
  private def centerContent = {
    val lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    lp.gravity = Gravity.CENTER
    lp
  }
}
