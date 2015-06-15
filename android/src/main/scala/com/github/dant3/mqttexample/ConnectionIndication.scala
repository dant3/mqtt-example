package com.github.dant3.mqttexample

import android.app.{PendingIntent, Service}
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.{NotificationCompat, NotificationManagerCompat}
import com.github.dant3.mqttexample.utils.avatars.MaterialPalette
import org.jivesoftware.smack.{ConnectionListener, XMPPConnection}
import org.jxmpp.util.XmppStringUtils

trait ConnectionIndication extends Service { self:ConnectionService ⇒
  private val notificationID = 0
  private lazy val notificationManager = NotificationManagerCompat.from(this)
  private var connectedConnection:Option[(XMPPConnection, ConnectionNotificator)] = None

  override def onCreate() = {
    super.onCreate()
    self.xmppConnection.subscribe { _ match {
      case Some(cn) ⇒
        disconnectListener()
        val notificator = new ConnectionNotificator(cn)
        if (cn.isConnected) notificator.connected(cn) else notificator.reconnectingIn(0)
        cn addConnectionListener notificator
        connectedConnection = Some((cn, notificator))
      case None ⇒ disconnectListener()
    }}
  }

  private def disconnectListener() = {
    connectedConnection.foreach(pair ⇒ pair._1.removeConnectionListener(pair._2))
    connectedConnection = None
  }

  override def onTaskRemoved(intent:Intent):Unit = {
    disconnectListener()
    notificationManager.cancel(notificationID)
    super.onTaskRemoved(intent)
  }

  class ConnectionNotificator(xmppConnection:XMPPConnection) extends ConnectionListener {
    private val notificationBuilder = createNotificationBuilder()

    override def connected(connection: XMPPConnection): Unit =
                            showNotification(MaterialPalette.GREEN, android.R.drawable.presence_online)
    override def reconnectionFailed(e: Exception): Unit = {}
    override def reconnectionSuccessful(): Unit = {}
    override def authenticated(connection: XMPPConnection, resumed: Boolean): Unit = {}
    override def connectionClosedOnError(e: Exception): Unit =
                            showNotification(MaterialPalette.RED, android.R.drawable.presence_busy)
    override def connectionClosed(): Unit = {}
    override def reconnectingIn(seconds: Int): Unit =
                            showNotification(MaterialPalette.ORANGE, android.R.drawable.presence_away)

    private def showNotification(color:Int, icon:Int) = {
      val userBareJid = XmppStringUtils.parseBareJid(xmppConnection.getUser)
      notificationBuilder.setContentText(userBareJid)
      notificationBuilder.setColor(color)
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        notificationBuilder.setSmallIcon(icon)
      }
      notificationManager.notify(notificationID, notificationBuilder.build())
    }
  }

  protected def notificationIntent:Intent

  private def createNotificationBuilder():NotificationCompat.Builder = {
    val builder = new NotificationCompat.Builder(this)
    builder.setAutoCancel(false)
    builder.setOngoing(true)
    builder.setContentIntent(PendingIntent.getActivity(getApplicationContext, 0, notificationIntent, 0))
    builder.setContentTitle(getResources.getString(R.string.app_name))
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      builder.setLargeIcon(BitmapFactory.decodeResource(getResources, R.drawable.ic_notification))
    } else {
      builder.setSmallIcon(R.drawable.ic_notification)
    }
    builder
  }
}