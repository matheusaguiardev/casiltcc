package br.com.aguiar.casil.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import br.com.aguiar.casil.notification.NotificationFactory
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.*


class Ear : Service() {

    private var ibinder: IBinder
    var buffer = ByteArray(1024)
    var onReceiveMessage: (T: String) -> Unit = {}

    var socket: MulticastSocket? = null


    init {
        this.ibinder = EarBinder()
    }

    private fun createPacket(): DatagramPacket {
        return DatagramPacket(buffer, buffer.size)
    }

    fun start(groupAddress: String, port: Int) {
        this.socket = createSocket(InetAddress.getByName(groupAddress), port)
    }

    private fun createSocket(groupAddress: InetAddress, port: Int): MulticastSocket {
        try {
            val newSocket = MulticastSocket(null)
            newSocket.bind(InetSocketAddress(port))
            Log.i("SOCKET", "Binding na porta ${port}")
            newSocket.timeToLive = 128
            newSocket.broadcast = true
            newSocket.joinGroup(groupAddress)
            Log.i("SOCKET", "Entrou no grupo ${groupAddress}")
            return newSocket
        } catch (e: SocketException) {
            e.printStackTrace()
            throw Exception(e.message)
        }
    }

    fun startListening() {

        doAsync {

            val packet = createPacket()
            Log.i("SOCKET", "Aguardando mensagem")
            socket?.receive(packet)
            Log.i("SOCKET", "Mensagem recebida")
            val msg = String(packet!!.getData(), packet!!.getOffset(), packet!!.getLength())
            Log.i("MSG", msg)
            val notification = NotificationFactory<Ear>()
            //notification.generateNotification()
            //socket?.close()
            uiThread {
                doReceive(msg)
            }
            startListening()
        }
    }

    fun doReceive(msg: String) {
        this.onReceiveMessage(msg)
    }

    override fun onBind(intent: Intent): IBinder? {
        return ibinder
    }

    inner class EarBinder : Binder() {
        val instance: Ear
            get() = this@Ear
    }

}
