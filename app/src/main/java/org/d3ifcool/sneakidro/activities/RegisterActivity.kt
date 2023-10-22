package org.d3ifcool.sneakidro.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.d3ifcool.sneakidro.R
import org.d3ifcool.sneakidro.data.DATA_USERS
import org.d3ifcool.sneakidro.data.User
import org.d3ifcool.sneakidro.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sudahPunyaAkun.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://sneakidro-b5c47-default-rtdb.asia-southeast1.firebasedatabase.app/")

        binding.registerButton.setOnClickListener { register() }
    }

    private fun register() {
        val nama = binding.namaInput.text.toString()
        val noHp = binding.nomorInput.text
        val email = binding.emailInput.text
        val password = binding.passwordInput.text

        if (nama.isEmpty()) {
            binding.namaInput.error = getString(R.string.nama_kosong)
            binding.namaInput.requestFocus()
            return
        }

        if(nama.length < 5){
            binding.namaInput.error = getString(R.string.panjang_nama)
            binding.namaInput.requestFocus()
            return
        }

        if (email.isEmpty()) {
            binding.emailInput.error = getString(R.string.email_inv)
            binding.emailInput.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailInput.error = getString(R.string.email_inv)
            binding.emailInput.requestFocus()
            return
        }

        if (noHp.isEmpty()) {
            binding.nomorInput.error = getString(R.string.no_hp_kosong)
            binding.nomorInput.requestFocus()
            return
        }
        //jika phone number kurang dari 12
        if (noHp.length <= 10){
            binding.nomorInput.error = getString(R.string.panjang_nomor)
            binding.nomorInput.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.passwordInput.error = getString(R.string.pass_kosong)
            binding.passwordInput.requestFocus()
            return
        }

        if(password.length < 6){
            binding.passwordInput.error = getString(R.string.pass_min_length)
            binding.passwordInput.requestFocus()
            return
        }
        checkUsernameExist()
    }


    private fun checkUsernameExist(){
        val nama = binding.namaInput.text.toString()
        val ref = database.reference.child("username/$nama")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(nama == snapshot.value.toString()){
                    binding.namaInput.error = getString(R.string.nama_exist)
                    binding.namaInput.requestFocus()
                    return
                }else{
                    firebaseRegister()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun firebaseRegister(){
        val nama = binding.namaInput.text.toString()
        val noHp = binding.nomorInput.text.toString()
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()
        val address = ""

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            if (it.isSuccessful){
                val userId = auth.currentUser?.uid ?: ""
                val databaseRef = database.reference.child(DATA_USERS).child(userId)
                val user = User(auth.uid,nama, email, password, noHp, address)
                val ref = database.reference.child("username/$nama")
                ref.setValue(nama)

                databaseRef.setValue(user).addOnCompleteListener{
                    if (it.isSuccessful){
                        Toast.makeText(this,"Registrasi berhasil",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,SneakidroActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this,"Coba lagi nanti",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Email already used.",Toast.LENGTH_SHORT).show()

            }
        }
    }
}