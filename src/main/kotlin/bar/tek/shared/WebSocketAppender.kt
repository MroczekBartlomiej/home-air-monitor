package bar.tek.shared

import bar.tek.logChannel
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WebSocketAppender : AppenderBase<ILoggingEvent>() {
    private lateinit var encoder: PatternLayoutEncoder

    override fun start() {
        super.start()
        encoder.start()
    }

    override fun stop() {
        super.stop()
        encoder.stop()
    }

    override fun append(eventObject: ILoggingEvent?) {
        eventObject?.let {
            val message = encoder.encode(it)
            GlobalScope.launch {
                logChannel.send(String(message))
            }
        }
    }

    fun setEncoder(encoder: PatternLayoutEncoder) {
        this.encoder = encoder
    }
}