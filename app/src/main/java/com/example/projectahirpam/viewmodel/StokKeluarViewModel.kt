package com.example.projectahirpam.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectahirpam.data.database.AppDatabase
import com.example.projectahirpam.data.dao.StokKeluarDao
import com.example.projectahirpam.data.dao.KategoriDao
import com.example.projectahirpam.data.entity.BarangEntity
import com.example.projectahirpam.data.entity.KategoriEntity
import com.example.projectahirpam.data.entity.StokKeluarEntity
import com.example.projectahirpam.utils.UserSession
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StokKeluarViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val dao: StokKeluarDao = db.stokKeluarDao()
    private val barangDao = db.barangDao()
    private val kategoriDao: KategoriDao = db.kategoriDao()
    private val userId = UserSession(application).getUserId()

    val list = dao.getAll(userId)

    private suspend fun defaultKategoriId(): Int {
        val name = "Umum"
        val existing = kategoriDao.getByName(userId, name)
        return existing?.id ?: kategoriDao.insert(KategoriEntity(namaKategori = name, userId = userId)).toInt()
    }

    private fun nowString(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
    }

    fun tambah(nama: String, jumlah: Int, kategoriId: Int) {
        viewModelScope.launch {
            try {
                dao.insert(StokKeluarEntity(namaBarang = nama, jumlah = jumlah, tanggal = nowString(), userId = userId))
                val existing = barangDao.getByName(userId, nama)
                if (existing != null) {
                    val newJumlah = (existing.jumlah - jumlah).coerceAtLeast(0)
                    val updated = existing.copy(jumlah = newJumlah, updatedAt = nowString())
                    barangDao.update(updated)
                    
                    // Log untuk debugging
                    if (newJumlah == 0) {
                        println("INFO: Stok '$nama' sekarang habis (0)")
                    }
                } else {
                    val katId = kategoriId
                    val b = BarangEntity(userId = userId, namaBarang = nama.trim(), kategoriId = katId, updatedAt = nowString())
                    barangDao.insert(b)
                }
            } catch (e: Exception) {
                println("ERROR saat menambah stok keluar: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
