package com.github.dant3.mqttexample

import android.app.Service
import android.content.Intent
import com.github.dant3.mqttexample.ui.TimeActivity
import com.github.dant3.mqttexample.utils.Logger
import com.github.dant3.mqttexample.utils.android.ServiceLog
import com.github.dant3.mqttexample.utils.android.UiImplicits._
import com.github.dant3.mqttexample.utils.android.concurrent.AsyncExecutor
import com.github.dant3.mqttexample.utils.android.service.{BoundService, ServiceBinder}
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import rx.lang.scala.{RxVal, RxVar}

import scala.concurrent.Future

class ConnectionService extends Service with ConnectionIndication with ServiceLog with BoundService[ConnectionService] with Logger {
  import ConnectionService._

  private var _connection:RxVar[Option[AbstractXMPPConnection]] = RxVar(None)
  val xmppConnection:RxVal[Option[AbstractXMPPConnection]] = _connection

  override def onTaskRemoved(rootIntent: Intent): Unit = {
    cleanup()
    super.onTaskRemoved(rootIntent)
  }

  override def onDestroy(): Unit = {
    cleanup()
    super.onDestroy()
  }

  private def cleanup() = {
    log.debug("Disconnecting xmpp connection...")
    xmppConnection.get.foreach(shutdownXmppClient)
    _connection := None
  }

  def startConnection(jid:String, password:String):Future[Unit] = {
    val conn = createXmppClient(jid, password)
    connectAndLogin(conn).mapUi { connection ⇒
      _connection := Some(connection)
    }
  }

  override protected def notificationIntent: Intent = new Intent(this, classOf[TimeActivity])
}

object ConnectionService extends ServiceBinder[ConnectionService] {
  private def shutdownXmppClient(xmppClient:AbstractXMPPConnection) = { xmppClient.disconnect() }
  private def createXmppClient(jid:String, password:String) = new XMPPTCPConnection(jid, password)
  private def connectAndLogin[T <: AbstractXMPPConnection](xmppConnection:T):Future[T] = {
    implicit val context = AsyncExecutor.context
    Future {
      xmppConnection.connect()
      xmppConnection.login()
      xmppConnection
    }
  }
}
