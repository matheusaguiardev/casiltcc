package br.com.aguiar.casil.model

/**
 * Created by aguiar on 03/08/17.
 */
class WiFiInfo constructor(ssid: String, bssid: String, password: String?) {
    var ssid = ssid
    var bssid = bssid
    var password: String? = password
}