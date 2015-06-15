package com.github.dant3.mqttexample.application

import java.io.File

import com.github.dant3.mqttexample.utils.Logger

class FatalExceptionHandler private (parent:Option[Thread.UncaughtExceptionHandler],
                                     shutdownHooks:Seq[FatalExceptionHandler.ShutdownHook])
                                                                              extends Thread.UncaughtExceptionHandler {
  import FatalExceptionHandler.{logger ⇒ log}

  override def uncaughtException(thread: Thread, ex: Throwable): Unit = try {
    if (ex.isInstanceOf[OutOfMemoryError]) {
      log.error("Application crashed! Reason is OutOfMemoryError.")
      log.error("Application crashed! Last frames are: {}", lastStackFrames(thread, 1))
    } else {
      log.error("Application crashed! Reason is:", ex)
    }
    invokeShutdownHooks();
  } catch {
    case err:Throwable ⇒ log.error("Unknown failure while trying to shutdown app: ", err)
  } finally {
    handleWithParent(thread, ex)
  }

  private def lastStackFrames(thread:Thread, n:Int):Array[StackTraceElement] = {
    val shortStackTrace = new Array[StackTraceElement](n)
    /* We start from index 3 because:
     * +1 is for getStackTrace
     * +1 is for lastStackFrames
     * +1 is for caller method - a place where this method was called
     */
    thread.getStackTrace.copyToArray(shortStackTrace, 3)
    shortStackTrace
  }

  private def handleWithParent(thread:Thread, ex:Throwable) = parent match {
    case Some(handler) ⇒ handler.uncaughtException(thread, ex)
    case None ⇒
  }

  private def invokeShutdownHooks() = {
    log.debug("Invoking shutdown hooks: {}", shutdownHooks.size)
    for (shutdownHook ← shutdownHooks) try {
      shutdownHook.apply()
    } catch {
      case error:Throwable ⇒ log.error(s"Shutdown hook $shutdownHook failed! ", error);
    }
  }
}


object FatalExceptionHandler extends Logger {
  private def logger = this.log

  type ShutdownHook = () ⇒ Any

  def setup():Unit = setup(Seq.empty)

  def setup(shutdownHooks:Seq[ShutdownHook]):Unit = {
    Thread.setDefaultUncaughtExceptionHandler(FatalExceptionHandler(shutdownHooks))
  }

  private def apply(shutdownHooks:Seq[ShutdownHook]):FatalExceptionHandler = {
    new FatalExceptionHandler(Option(Thread.getDefaultUncaughtExceptionHandler), shutdownHooks)
  }

  def dumpMemory(dumpFile:File) = {
    val filePath = dumpFile.getAbsolutePath
    log.warn("Dumping memory to {} ...", filePath)
     android.os.Debug.dumpHprofData(filePath)
  }
}