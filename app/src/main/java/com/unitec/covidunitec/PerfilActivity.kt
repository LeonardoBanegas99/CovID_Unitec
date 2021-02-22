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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.main_frame.*
import org.jetbrains.anko.toast
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PerfilActivity : AppCompatActivity() {
    var btn_escaneo: Button? = null
    var lista: ListView? = null
    var text_numCuenta: TextView? = null
    var arrayList: ArrayList<String> = ArrayList()
    var arrayAdapter: ArrayAdapter<String>? = null
    var adapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var address : String? = null;
    private val db = FirebaseFirestore.getInstance()
    private var num_cuenta : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_frame)

        text_numCuenta = findViewById(R.id.user)
        btn_escaneo = findViewById(R.id.btn_estado)
        lista = findViewById(R.id.list_signals)
        num_cuenta = intent.extras!!.get("num_cuenta") as String
        text_numCuenta!!.text = num_cuenta.toString()

        btn_escaneo!!.setOnClickListener {
            //db.collection("usuarios").document("wv89Sx5nNmLs2CqIPPWU").get().addOnSuccessListener {
            //user.setText(it.get("num_cuenta") as String?)
            //user.setText("pp")
            //}
            arrayList.clear()
            arrayAdapter = ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_1, arrayList)
            lista!!.setAdapter(arrayAdapter)
            var bool = adapter.startDiscovery()
            toast("Click " + bool)
        };

        var filter: IntentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
        /*for (e in arrayList) {
            d("device", e + "----------------------")
        }*/

    }

    var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                var nombre = device!!.name
                var dir = device!!.address
                arrayList.add("Nombre: " + nombre + " --- dir: " + dir)

                val sdf = SimpleDateFormat("d.M.yyyy hh aaa")
                val currentDate = sdf.format(Date()).toString()
                var id_interaction : String = num_cuenta + "-" + dir.toString() + "-" + currentDate

                var interaction = interaction(num_cuenta!!,dir,currentDate)

                db.collection("interactions")
                    .document(id_interaction)
                    .set(interaction)
                    .addOnSuccessListener {
                        d("exito", "exito")
                    }.addOnFailureListener {
                        it.message?.let { it1 -> d("fracaso", it1) }
                    }

                arrayAdapter!!.notifyDataSetChanged()
            }
        }
    }

}
