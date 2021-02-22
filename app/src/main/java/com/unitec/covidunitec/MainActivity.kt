package com.unitec.covidunitec

import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.util.Log.d
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_IniciarSesion.setOnClickListener {
            //db.collection("users").document("num_cuenta").set(
            //hashMapOf("numero de cuenta" to "prueba", "contraseña" to "prueba")
            //)
            // ----------------    no borren esto xfa  ----------------------

            // ET_contraLogin.setText(it.get("contraseña") as String?)
            /*var num_cuentaBD = (it.get("num_cuenta") as String?).toString()
            var contraBD = (it.get("contraseña") as String?).toString()*/
            var num_cuentaET = ET_usuarioLogin.text.toString()
            var contraET = ET_contraLogin.text.toString()

            var contraEnc = encriptar(contraET,contraET)
            //d("encriptado",contraEnc)
            if (num_cuentaET.isNotEmpty() && contraET.isNotEmpty()) {
                db.collection("users")
                    .whereEqualTo("num_cuenta",num_cuentaET)
                    .whereEqualTo("contraseña",contraEnc)
                    .get()
                    .addOnSuccessListener { result ->
                        val intent = Intent(this, PerfilActivity::class.java)
                        intent.putExtra("num_cuenta",num_cuentaET)
                        ET_usuarioLogin.setText("")
                        ET_contraLogin.setText("")
                        startActivity(intent)
                        overridePendingTransition(R.anim.fade_in, R.anim.nothing)
                        finish()
                    }.addOnFailureListener {
                        noti_error()
                    }
            } else {
                camposvacios()
            }
        }
    }

    private fun camposvacios() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Campo vacio")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun noti_error() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Usuario no existe o contraseña incorrecta")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    @Throws(Exception::class)
    private fun encriptar(datos: String, password: String): String {
        //Se obtiene la llave mediante el método generateKey()
        val secretKey = generateKey(password)

        //Se obtiene el cifrado mediante una instancia de la clase Cipher y la llave generada
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val datosEncriptadosBytes = cipher.doFinal(datos.toByteArray())
        return Base64.encodeToString(datosEncriptadosBytes, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    private fun generateKey(password: String): SecretKeySpec {
        val sha = MessageDigest.getInstance("SHA-256")
        var key = password.toByteArray(charset("UTF-8"))
        key = sha.digest(key)
        return SecretKeySpec(key, "AES")
    }
}