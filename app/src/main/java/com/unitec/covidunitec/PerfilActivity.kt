package com.unitec.covidunitec

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log.d
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_frame.*
import org.jetbrains.anko.toast
import java.util.*
import kotlin.collections.ArrayList


class PerfilActivity : AppCompatActivity() {
    var btn_escaneo: Button? = null
    var lista : ListView? = null
    var arrayList : ArrayList<String> = ArrayList()
    var arrayAdapter : ArrayAdapter<String>? = null
    var adapter : BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_frame)

        btn_escaneo = findViewById(R.id.btn_estado)
        lista = findViewById(R.id.list_signals)

        btn_escaneo!!.setOnClickListener {
            var bool = adapter.startDiscovery()
            toast("Click " + bool)
        };

        var filter: IntentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
        for (e in arrayList){
            d("device",e.toString())
        }
        arrayAdapter = ArrayAdapter<String>(getApplicationContext(),R.layout.simple_list_item_1,arrayList)
        lista!!.setAdapter(arrayAdapter)
    }

    var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                arrayList.add(device!!.name)
                toast(device!!.name)
                arrayAdapter!!.notifyDataSetChanged()
            }
        }
    }
}
