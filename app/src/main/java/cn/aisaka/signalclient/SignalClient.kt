package cn.aisaka.signalclient

import SignalMessageFactory
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import com.alibaba.fastjson.JSON
import okhttp3.*
import okio.ByteString

class SignalClient {
    val TAG = "SignalClient"
    private var handleThread: HandlerThread
    private var workHandler: Handler

    private var webSocket: WebSocket? = null

    var cb: OnSignalCallback? = null
    private var mainHandler: Handler = object : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            when (msg.what) {
                0 -> {}
                1 -> {
                    cb?.onMessage(msg.obj.toString())
                }
                2 -> {}
                3 -> {}
            }

        }
    }

    init {
        handleThread = HandlerThread("ws_signal")
        handleThread.start()
        workHandler = Handler(handleThread.looper)
    }


    fun start() {
        workHandler.post { startSignal() }
    }

    fun sendMsg() {
        workHandler.post { webSocket?.send(JSON.toJSONString(SignalMessageFactory.buildJoinRoom("666"))) }
    }

    private fun startSignal() {

        webSocket = OkHttpClient().newWebSocket(
            Request.Builder().url("ws://192.168.2.59:8888").build(),
            object :
                WebSocketListener() {
                /**
                 * Invoked when a web socket has been accepted by the remote peer and may begin transmitting
                 * messages.
                 */
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    Log.e(TAG, "onOpen")
                    mainHandler.sendMessage(Message.obtain(mainHandler, 0))

                }

                /** Invoked when a text (type `0x1`) message has been received. */
                override fun onMessage(webSocket: WebSocket, text: String) {
                    Log.e(TAG, "onMessage")


                    mainHandler.sendMessage(Message.obtain(mainHandler, 1, text))

                }

                /** Invoked when a binary (type `0x2`) message has been received. */
                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    mainHandler.sendMessage(Message.obtain(mainHandler, 2, bytes))

                }

                /**
                 * Invoked when the remote peer has indicated that no more incoming messages will be transmitted.
                 */
                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    Log.e(TAG, "onClosing")
                    mainHandler.sendMessage(Message.obtain(mainHandler, 3, code))

                }

                /**
                 * Invoked when both peers have indicated that no more messages will be transmitted and the
                 * connection has been successfully released. No further calls to this listener will be made.
                 */
                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    Log.e(TAG, "onClosed")
                    mainHandler.sendMessage(Message.obtain(mainHandler, 4, code))

                }

                /**
                 * Invoked when a web socket has been closed due to an error reading from or writing to the
                 * network. Both outgoing and incoming messages may have been lost. No further calls to this
                 * listener will be made.
                 */
                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    Log.e(TAG, "onFailure ${t.message}\t${response?.message}")
                    mainHandler.sendMessage(Message.obtain(mainHandler, 5, t))

                }
            })

    }

    interface OnSignalCallback {
        fun onMessage(msg: String)
    }
}