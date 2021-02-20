package com.unitec.covidunitec

import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    var var1 = null
    val var2 = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_IniciarSesion.setOnClickListener {
            //db.collection("users").document("num_cuenta").set(
                    //hashMapOf("numero de cuenta" to "prueba", "contraseña" to "prueba")
            //)
            // ----------------    no borren esto xfa  ----------------------
           if(ET_usuarioLogin.text.isNotEmpty() &&  ET_contraLogin.text.isNotEmpty()){
               db.collection("users").document(ET_usuarioLogin.text.toString()).get().addOnSuccessListener {
                   // ET_contraLogin.setText(it.get("contraseña") as String?)
                    var a =  (it.get("num_cuenta") as String?).toString()
                    var b =  (it.get("contraseña") as String?).toString()
                    if(a.equals( (ET_usuarioLogin.text).toString()) && b.equals((ET_contraLogin.text).toString()) ){
                        val intent = Intent(this,PerfilActivity::class.java)
                        startActivity(intent)
                    }else{
                        noti_error()
                    }
                }
           } else {
                camposvacios()
           }


          //  val intent = Intent(this,PerfilActivity::class.java)
          //  startActivity(intent)

        }




    }

    private fun camposvacios(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Campo vacio")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun noti_error(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Usuario no existe o contraseña incorrecta")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}