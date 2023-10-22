package org.d3ifcool.sneakidro.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3ifcool.sneakidro.repository.MyRepository

//class untuk menangkap class MyRepository dan melanjutkan ke OrderAdapter lalu ke OrderFragment
class MainViewModel : ViewModel() {
    private val repository : MyRepository = MyRepository().getInstance()
    private val _allPemesanan = MutableLiveData<List<Pemesanan>>()
    val allPemesanan : LiveData<List<Pemesanan>> = _allPemesanan

    init {
        repository.loadPemesanan(_allPemesanan)
    }
}
