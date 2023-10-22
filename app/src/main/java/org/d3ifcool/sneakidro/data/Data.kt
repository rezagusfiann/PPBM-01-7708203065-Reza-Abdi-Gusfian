package org.d3ifcool.sneakidro.data

data class User(
    val uid: String? = "",
    val name: String? = "",
    val email: String? = "",
    val password: String? = "",
    val number: String? = "",
    val address: String? = "",
    val imageUrl: String? = ""
)

data class Pemesanan(
    val kategori: String? = "",
    val sepatu :String? = "",
    val jumlah :Int? = 0,
    val cttn :String? = "",
    val metode :String? = "",
    val userId :String? = "",
    var idPemesanan: String? = "",
    val harga: Int? = 0,
    val statusPembayaran: String? ="",
)