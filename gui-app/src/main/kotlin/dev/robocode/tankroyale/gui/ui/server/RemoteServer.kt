package dev.robocode.tankroyale.gui.ui.server

import dev.robocode.tankroyale.gui.settings.ServerSettings
import java.net.URI
import java.net.http.HttpClient
import java.net.http.WebSocket
import java.net.http.WebSocket.Listener
import java.time.Duration

object RemoteServer {

    private val DEFAULT_CONNECTION_TIMEOUT = Duration.ofMillis(3000)

    fun isRunning(uri: String = ServerSettings.serverUrl, timeout: Duration? = DEFAULT_CONNECTION_TIMEOUT): Boolean =
        isRunning(URI(uri), timeout)

    fun isRunning(uri: URI, timeout: Duration? = DEFAULT_CONNECTION_TIMEOUT): Boolean {
        return try {
            val httpClient = HttpClient.newBuilder().build()
            val webSocketBuilder = httpClient.newWebSocketBuilder().connectTimeout(timeout)
            webSocketBuilder.buildAsync(uri, object : Listener {
                override fun onOpen(webSocket: WebSocket) {
                    webSocket.abort() // close connection
                }
            }).join()
            true
        } catch (ex: Exception) {
            false
        }
    }
}

fun main() {
    val isRunning = RemoteServer.isRunning(URI(ServerSettings.serverUrl))
    println("isRunning: $isRunning")
}