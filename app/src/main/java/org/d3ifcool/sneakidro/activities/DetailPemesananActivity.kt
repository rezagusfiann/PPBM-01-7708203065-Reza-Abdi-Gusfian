package org.d3ifcool.sneakidro.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.d3ifcool.sneakidro.data.*
import org.d3ifcool.sneakidro.databinding.ActivityDetailPemesananBinding
import java.text.DecimalFormat
import java.text.NumberFormat

class DetailPemesananActivity : AppCompatActivity() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: ActivityDetailPemesananBinding
    private val ref = FirebaseDatabase.getInstance().reference
    private val userId = firebaseAuth.currentUser?.uid ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPemesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val kategori = intent.getStringExtra("kategori")
        val sepatu = intent.getStringExtra("sepatu")
        val harga = intent.getStringExtra("harga")
        val jumlah = intent.getStringExtra("jumlah")
        val metode = intent.getStringExtra("metode")
        val status = intent.getStringExtra("statusPembayaran")

        // perkalian harga dan jumlah
        val h = Integer.parseInt(harga.toString())
        val j = Integer.parseInt(jumlah.toString())
        val totalHarga = h * j

        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(totalHarga)

        // penkondisian harga di detail pemesanan
        if(kategori == DEEP_CLEANING) {
            binding.tvKategori.text = kategori
            binding.tvJenisSepatu.text = sepatu
            binding.tvJumlahSepatu.text = jumlah
            binding.nMetode.text = metode
            binding.tvTotalHarga.text = formattedNumber
            binding.hargaCleaning.text = formatter.format(h)
            binding.tvStatus.text = status
        } else if (kategori == SOFT_CLEANING) {
            binding.tvKategori.text = kategori
            binding.tvJenisSepatu.text = sepatu
            binding.tvJumlahSepatu.text = jumlah
            binding.nMetode.text = metode
            binding.tvTotalHarga.text = formattedNumber
            binding.hargaCleaning.text = formatter.format(h)
            binding.tvStatus.text = status
        }

        //pengkondisian jika pembayaran Transfer BCA
        if (metode == BCA) {
            binding.norek.visibility = View.VISIBLE
        }

        binding.backPemesanan.setOnClickListener{
            finish()
        }

        binding.fabBtn.setOnClickListener{
            lanjutWa()
            finish()
        }

        binding.questionMark.setOnClickListener {
            val i = Intent(this, MekanismePembayaranActivity::class.java)
            startActivity(i)
        }

        ref.child("Users").child(userId).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val email = "${snapshot.child("email").value}"
                val name = "${snapshot.child("name").value}"
                val number = "${snapshot.child("number").value}"

                binding.npPemesan.text = name
                binding.nEmail.text = email
                binding.nHp.text = number

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun lanjutWa() {
        val intent = CustomTabsIntent.Builder().build()
        intent.launchUrl(this, Uri.parse(PESAN_WA))
    }

    companion object {
        private const val PESAN_WA =
            "https://wa.me/62895603566261"
    }

}