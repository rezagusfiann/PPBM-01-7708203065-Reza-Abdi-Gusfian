package org.d3ifcool.sneakidro.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import org.d3ifcool.sneakidro.R
import org.d3ifcool.sneakidro.databinding.ActivityLoginBinding

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            val intent = Intent(this, SneakidroActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(this.authStateListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getPermission()

        firebaseAuth = FirebaseAuth.getInstance()

        binding.belumPunyaAkun.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
            finish()
        }

        binding.loginButton.setOnClickListener { login()
            val email = binding.emailInput.text.toString()
            val pass = binding.passwordInput.text.toString()
            val loading = LoadingDialog(this)
            loading.startLoading()

            val handler = Handler()
            handler.postDelayed(object :Runnable {
                override fun run() {
                    loading.isDismiss()
                }
            },1500)

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        loading.isDismiss()
                    } else {
                        loading.isDismiss()
                    }
                }
            }
        }
    }

    private fun getPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }
    }

    private fun login(){
        val email = binding.emailInput.text
        val password = binding.passwordInput.text

        if(email.isEmpty() && password.isEmpty()){
            Toast.makeText(this, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()
            binding.emailInput.requestFocus()
            return
        }

        //validasi email
        if(email.isEmpty()){
            Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
            binding.emailInput.error = getString(R.string.email_inv)
            binding.emailInput.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Email tidak ada", Toast.LENGTH_SHORT).show()
            binding.emailInput.error = getString(R.string.email_inv)
            binding.email.requestFocus()
            return
        }

        //validasi password
        if(password.isEmpty()){
            Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            binding.passwordInput.requestFocus()
            return
        }
        firebaseLogin()
    }

    private fun firebaseLogin(){
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()

        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                Toast.makeText(this,"Login berhasil", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Email atau Password anda salah", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(this.authStateListener)
    }
}