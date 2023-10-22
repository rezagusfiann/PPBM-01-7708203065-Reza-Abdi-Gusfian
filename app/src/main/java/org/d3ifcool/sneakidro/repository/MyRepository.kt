package org.d3ifcool.sneakidro.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import org.d3ifcool.sneakidro.data.Pemesanan

//class untuk menangkap data dari firebase ke class ini dan akan dilanjutkan ke class MainViewModel lalu ke OrderAdapter
class MyRepository {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://sneakidro-b5c47-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Pemesanan")

    @Volatile
    private var INSTANCE: MyRepository? = null

    fun getInstance(): MyRepository {
        return INSTANCE ?: synchronized(this) {
            val instance = MyRepository()
            INSTANCE = instance
            instance
        }
    }

    fun loadPemesanan(pemesananList: MutableLiveData<List<Pemesanan>>) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val pesananList: List<Pemesanan> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Pemesanan::class.java)!!
                    }
                    pemesananList.postValue(pesananList)
                } catch (e : Exception) {
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}
