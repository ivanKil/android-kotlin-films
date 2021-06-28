package com.lessons.films.connectcheck

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.lessons.films.R

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val status = NetworkUtil.getConnectivityStatusString(context);
        if ("android.net.conn.CONNECTIVITY_CHANGE" == intent.getAction()) {
            if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
                Toast.makeText(context, context.getResources().getString(R.string.no_connect), Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(context, context.getResources().getString(R.string.connect_has), Toast.LENGTH_SHORT).show()
        }
    }
}