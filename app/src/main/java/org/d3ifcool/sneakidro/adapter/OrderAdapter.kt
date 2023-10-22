package org.d3ifcool.sneakidro.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import org.d3ifcool.sneakidro.R
import org.d3ifcool.sneakidro.activities.DetailPemesananActivity
import org.d3ifcool.sneakidro.data.DEEP_CLEANING
import org.d3ifcool.sneakidro.data.Pemesanan
import org.d3ifcool.sneakidro.data.SOFT_CLEANING
import java.text.DecimalFormat
import java.text.NumberFormat

//class untuk menangkap layout recycleview dari user_order_xml unutk di binding kan dengan database,
//dengan menghubungkan class MainViewModel
class OrderAdapter(private val pemesananList: ArrayList<Pemesanan>) :
    RecyclerView.Adapter<OrderAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.user_order_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem2 = pemesananList[position]
        val h = Integer.parseInt(currentitem2.harga.toString())
        val j = Integer.parseInt(currentitem2.jumlah.toString())
        val totalHarga = h * j

        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(totalHarga)

        // pengkondisian harga di order fragment
        if(currentitem2.kategori == DEEP_CLEANING) {
            holder.tipePemesanan.text = currentitem2.kategori.toString()
            holder.jenisSepatu.text = currentitem2.sepatu
            holder.jumlahSepatu.text = currentitem2.jumlah.toString()
            holder.totalHarga.text = formattedNumber
            holder.harga.text = formatter.format(currentitem2.harga)
            holder.status.text = currentitem2.statusPembayaran
        }
        else if(currentitem2.kategori == SOFT_CLEANING) {
            holder.tipePemesanan.text = currentitem2.kategori.toString()
            holder.jenisSepatu.text = currentitem2.sepatu
            holder.jumlahSepatu.text = currentitem2.jumlah.toString()
            holder.totalHarga.text = formattedNumber
            holder.harga.text = formatter.format(currentitem2.harga)
            holder.status.text = currentitem2.statusPembayaran
        }

        holder.itemView.setOnClickListener { v ->
            val intent = Intent(v.context, DetailPemesananActivity::class.java)
            intent.putExtra("metode", currentitem2.metode)
            intent.putExtra("kategori", currentitem2.kategori)
            intent.putExtra("sepatu", currentitem2.sepatu)
            intent.putExtra("harga", currentitem2.harga.toString())
            intent.putExtra("jumlah", currentitem2.jumlah.toString())
            intent.putExtra("docId", currentitem2.idPemesanan)
            intent.putExtra("statusPembayaran", currentitem2.statusPembayaran)
            v.context.startActivity(intent)
        }

        holder.cardRV.setOnLongClickListener {
            val option = arrayOf<CharSequence>(
                "Batalkan Pesanan",
                "Batal"
            )

            val builder: AlertDialog.Builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Opsi")
            builder.setItems(option) { _, which ->
                if (which == 0) {
                    batalkanPesanan(position, holder)
                }
            }
            builder.show()
            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return pemesananList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tipePemesanan: TextView = itemView.findViewById(R.id.tvKategori)
        val jenisSepatu: TextView = itemView.findViewById(R.id.tvJenisSepatu)
        val jumlahSepatu: TextView = itemView.findViewById(R.id.tvJumlahSepatu)
        val totalHarga : TextView = itemView.findViewById(R.id.tvTotalHarga)
        val harga : TextView = itemView.findViewById(R.id.hargaCleaning)
        val cardRV : CardView = itemView.findViewById(R.id.orderCardView)
        val status : TextView = itemView.findViewById(R.id.tvStatus)
    }

    private fun batalkanPesanan(position: Int , holder: MyViewHolder){
        val itemList= pemesananList[position]
        val ref = FirebaseDatabase.getInstance().reference.child("Pemesanan")
        val pos = itemList.idPemesanan!!

        Log.d("tag", pos)

        ref.child(pos)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(holder.itemView.context, "Pesanan Dibatalkan", Toast.LENGTH_SHORT).show()
            }
    }

}