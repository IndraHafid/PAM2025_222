package com.example.projectahirpam.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectahirpam.data.database.AppDatabase
import com.example.projectahirpam.data.entity.KategoriEntity
import com.example.projectahirpam.utils.UserSession
import kotlinx.coroutines.launch

class KategoriViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val dao = db.kategoriDao()
    private val userId = UserSession(application).getUserId()

    val kategoriList = dao.getAll(userId)

    fun tambahKategori(nama: String) {
        viewModelScope.launch {
            dao.insert(KategoriEntity(namaKategori = nama, userId = userId))
        }
    }

    fun hapusKategori(kategori: KategoriEntity) {
        viewModelScope.launch {
            dao.delete(kategori)
        }
    }
}
