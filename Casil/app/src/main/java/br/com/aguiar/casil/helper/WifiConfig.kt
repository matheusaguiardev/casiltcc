package br.com.aguiar.casil.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import br.com.aguiar.casil.model.WiFiInfo

/**
 * Created by aguiar on 03/08/17.
 */
class WifiConfig {

    fun getCurrentSsid(context: Context): WiFiInfo? {
        var informationWiFi: WiFiInfo? = null
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (networkInfo.isConnected) {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val connectionInfo = wifiManager.connectionInfo
            if (connectionInfo != null && !connectionInfo.ssid.isEmpty()) {

                informationWiFi = WiFiInfo(connectionInfo.ssid, connectionInfo.bssid, "")

            }
        }
        return informationWiFi
    }


}