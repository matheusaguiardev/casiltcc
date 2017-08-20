package br.com.aguiar.casil.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import br.com.aguiar.casil.R
import br.com.aguiar.casil.helper.WifiConfig
import br.com.aguiar.casil.interfaces.IServerControlSocket
import br.com.aguiar.casil.model.WiFiInfo
import br.com.aguiar.casil.service.Ear
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), View.OnClickListener, IServerControlSocket {

    var button: Button? = null
    var ssidWifi: EditText? = null
    var passwordWifi: EditText? = null
    private var mServiceConnected: Boolean = false
    private var mServiceIntent: Intent? = null
    var mService: Ear? = null

    val port: Int = 9090
    val ipBroadcast: String = "230.185.192.108"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = this.button_start
        ssidWifi = name_wifi
        passwordWifi = password_wifi

        button!!.setOnClickListener(this@MainActivity)

        this.mServiceIntent = Intent(this, Ear::class.java)
        startService(mServiceIntent)
    }

    override fun onStart() {
        super.onStart()
        bindService(mServiceIntent, mConnection, Context.BIND_AUTO_CREATE)
        fillFieldWifi()
    }

    private val mConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val earBinder: Ear.EarBinder = service as Ear.EarBinder
            mService = earBinder.instance
            mServiceConnected = true
            mService!!.onReceiveMessage = fun(msg: String) {
                toast(msg)
            }
            mService!!.start(ipBroadcast, port)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mServiceConnected = false
        }
    }

    fun fillFieldWifi() {
        val wifi: WifiConfig = WifiConfig()
        val wifiInfo = wifi.getCurrentSsid(this)
        ssidWifi!!.setText(wifiInfo?.ssid)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.button_start -> {
                if (mServiceConnected) {
                    mService!!.startListening()
                } else {
                    toast("Não consegui me conectar ao service !")
                }
            }
        }

    }

}