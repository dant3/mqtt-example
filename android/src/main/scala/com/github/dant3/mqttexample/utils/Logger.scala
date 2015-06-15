package com.github.dant3.mqttexample.utils

import ch.qos.logback.classic.android.LogcatAppender
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.{AsyncAppender, Level}
import ch.qos.logback.core.Appender
import com.github.dant3.mqttexample.BuildConfig
import org.slf4j.LoggerFactory

trait Logger {
  protected val log = LoggerFactory.getLogger(getClass)
}

object Logger {
  import ch.qos.logback.classic.{Logger ⇒ LogbackLogger, LoggerContext}
  import org.slf4j.{Logger ⇒ Slf4JLog}

  def setup(logcatTag:String):Unit = {
    val rootLogger = LoggerFactory.getLogger(Slf4JLog.ROOT_LOGGER_NAME).asInstanceOf[LogbackLogger]
    val loggerContext:LoggerContext = rootLogger.getLoggerContext
    loggerContext.reset()

    rootLogger.addAppender(async(loggerContext, logcatAppender(logcatTag, loggerContext)))
    if (BuildConfig.DEBUG) {
      rootLogger.setLevel(Level.DEBUG)
    } else {
      rootLogger.setLevel(Level.ERROR)
    }
  }

  private def async(loggerContext:LoggerContext, baseAppender:Appender[ILoggingEvent]):Appender[ILoggingEvent] = {
    val wrapper = new AsyncAppender()
    wrapper.setContext(loggerContext)
    wrapper.addAppender(baseAppender)
    wrapper.start()
    wrapper.setIncludeCallerData(true)
    wrapper
  }

  private def logcatAppender(logcatTag:String, loggerContext: LoggerContext) = {
    val appender = new LogcatAppender()
    appender.setContext(loggerContext)
    appender.setEncoder(createPatternLayoutEncoder(
      "[%thread{}] %logger{20}.%method{}\\(\\):%line{} - %msg%xThrowable{full}%n{}", loggerContext
    ))
    appender.setTagEncoder(createPatternLayoutEncoder(logcatTag, loggerContext))
    appender.start()
    appender
  }

  private def createPatternLayoutEncoder(pattern:String, loggerContext:LoggerContext):PatternLayoutEncoder = {
    val exceptionPatternLayoutEncoder = new PatternLayoutEncoder()
    exceptionPatternLayoutEncoder.setContext(loggerContext)
    exceptionPatternLayoutEncoder.setPattern(pattern)
    exceptionPatternLayoutEncoder.start()
    exceptionPatternLayoutEncoder
  }
}