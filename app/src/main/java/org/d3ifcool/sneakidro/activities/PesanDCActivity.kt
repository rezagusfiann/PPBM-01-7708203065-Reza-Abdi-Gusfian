package org.d3ifcool.sneakidro.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.d3ifcool.sneakidro.R
import org.d3ifcool.sneakidro.data.*
import org.d3ifcool.sneakidro.databinding.ActivityPesanDcBinding

class PesanDCActivity : AppCompatActivity() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var radioGroup1: RadioGroup? = null
    private var radioGroup2: RadioGroup? = null
    private var radioButton1: RadioButton? = null
    private var radioButton2: RadioButton? = null

    private val firebaseDatabase =
        FirebaseDatabase.getInstance("https://sneakidro-b5c47-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
    private lateinit var binding: ActivityPesanDcBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesanDcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        radioGroup1 = findViewById(R.id.radioGrup1)
        radioGroup2 = findViewById(R.id.radioGrup2)
        val docId = firebaseDatabase.push().key.toString()

        //fungsi bck button
        binding.backButton.setOnClickListener {
            finish()
        }

        //fungsi klik pesan button
        binding.pesanBtn.setOnClickListener {
            val selectedId1 = radioGroup1!!.checkedRadioButtonId
            val jenisSepatu = binding.jenisSepatu.text.toString()
            val jumlahSepatu = binding.jumlahSepatu.text.toString()
            val catatan = binding.catatan.text.toString()
            val selectedId2 = radioGroup2!!.checkedRadioButtonId
            val statusPembayaran = "Belum Bayar"
            val harga = 40000

            radioButton1 = findViewById(selectedId1) as? RadioButton
            radioButton2 = findViewById(selectedId2) as? RadioButton

            if (selectedId1 == -1 || jenisSepatu.isEmpty() ||
                jumlahSepatu.isEmpty() ||
                catatan.isEmpty() ||
                selectedId2 == -1
            ) {
                Toast.makeText(this, "Please Complete your order", Toast.LENGTH_SHORT).show()
            } else {
                val kategori = if (binding.deepClean.isChecked) DEEP_CLEANING else SOFT_CLEANING
                val metodePembayaran = if (binding.cashOnDelivery.isChecked) COD else BCA
                val userId = firebaseAuth.currentUser?.uid ?: ""
                val pemesanan = Pemesanan(
                    kategori,
                    jenisSepatu,
                    jumlahSepatu.toInt(),
                    catatan,
                    metodePembayaran,
                    userId,
                    docId,
                    harga,
                    statusPembayaran
                )
                //intent activity to sneakidroactivity
                finish()

                //insert data to realtime database
                firebaseDatabase.child(DATA_PEMESANAN).child(docId).setValue(pemesanan)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Pesanan Sudah Masuk", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(this, "Pesanan Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

}