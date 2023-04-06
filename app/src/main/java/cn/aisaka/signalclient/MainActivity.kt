package cn.aisaka.signalclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import org.webrtc.PeerConnectionFactory

class MainActivity : AppCompatActivity() {
    private lateinit var signalClient: SignalClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signalClient = SignalClient()
        signalClient.start()
        signalClient.cb = object : SignalClient.OnSignalCallback {
            override fun onMessage(msg: String) {
                Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
            }
        }

        findViewById<Button>(R.id.tv).setOnClickListener {
            signalClient.sendMsg()
        }
    }
}