package com.unitec.covidunitec

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_IniciarSesion.setOnClickListener {

            db.collection("users").document("num_cuenta").set(
                hashMapOf("numero de cuenta" to "prueba", "contraseña" to "prueba")
            )

            //val intent = Intent(this,PerfilActivity::class.java)
            //startActivity(intent)

        }




    }
}