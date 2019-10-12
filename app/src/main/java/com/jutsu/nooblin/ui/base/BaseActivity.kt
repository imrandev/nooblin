package com.jutsu.nooblin.ui.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    @LayoutRes
    abstract fun getLayoutRes() : Int
    abstract fun onConnected(isOnline : Boolean)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
        initNetwork()
    }

    override fun onStart() {
        super.onStart()
//        registerReceiver(broadcastReceiver,
//            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private val broadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                onConnected( isNetwork())
            }
        }
    }

    override fun onDestroy() {
        //unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    private fun isNetwork() : Boolean {
        val cm : ConnectivityManager = this.
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo;
        return activeNetwork != null && activeNetwork.isConnected
    }

    private fun initNetwork() {
        val connectivityManager : ConnectivityManager = this.
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val builder : NetworkRequest.Builder = NetworkRequest.Builder()
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        builder.addTransportType(NetworkCapabilities.TRANSPORT_VPN)
        connectivityManager.registerNetworkCallback(builder.build(), callback)
    }

    private val callback : ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network) {
            onConnected(true)
        }

        override fun onLost(network: Network) {
            onConnected(false)
        }
    }
}