package com.example.projectahirpam.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectahirpam.data.database.AppDatabase
import com.example.projectahirpam.data.dao.BarangManualDao
import com.example.projectahirpam.data.dao.KategoriDao
import com.example.projectahirpam.data.entity.BarangManualEntity
import com.example.projectahirpam.data.entity.KategoriEntity
import com.example.projectahirpam.utils.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class BarangFormState(
    val nama: String = "",
    val jumlah: String = "",
    val kategoriId: Int? = null,
    val isEditing: Boolean = false,
    val editingId: Int? = null
)

class BarangManualViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val dao: BarangManualDao = db.barangManualDao()
    private val kategoriDao: KategoriDao = db.kategoriDao()
    private val userId = UserSession(application).getUserId()

    val list = dao.getAll(userId)

    private val _formState = MutableStateFlow(BarangFormState())
    val formState: StateFlow<BarangFormState> = _formState.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private fun nowString(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
    }

    fun updateFormState(state: BarangFormState) {
        _formState.value = state
    }

    fun showAddDialog() {
        _formState.value = BarangFormState()
        _showDialog.value = true
    }

    fun showEditDialog(barang: BarangManualEntity) {
        _formState.value = BarangFormState(
            nama = barang.namaBarang,
            jumlah = barang.jumlah.toString(),
            kategoriId = barang.kategoriId,
            isEditing = true,
            editingId = barang.id
        )
        _showDialog.value = true
    }

    fun hideDialog() {
        _showDialog.value = false
        _errorMessage.value = ""
    }

    fun saveBarang() {
        val state = _formState.value
        
        // Validasi
        if (state.nama.isBlank()) {
            _errorMessage.value = "Nama barang wajib diisi!"
            return
        }
        
        val jumlahInt = state.jumlah.toIntOrNull()
        if (jumlahInt == null || jumlahInt < 0) {
            _errorMessage.value = "Jumlah harus berupa angka positif!"
            return
        }

        viewModelScope.launch {
            try {
                if (state.isEditing && state.editingId != null) {
                    // Update existing
                    val existing = dao.getById(state.editingId)
                    if (existing != null) {
                        val updated = existing.copy(
                            namaBarang = state.nama.trim(),
                            jumlah = jumlahInt,
                            kategoriId = state.kategoriId,
                            updatedAt = nowString()
                        )
                        dao.update(updated)
                    }
                } else {
                    // Check for duplicate
                    val existing = dao.getByName(userId, state.nama.trim())
                    if (existing != null) {
                        _errorMessage.value = "Barang dengan nama ini sudah ada!"
                        return@launch
                    }
                    
                    // Insert new
                    val newBarang = BarangManualEntity(
                        userId = userId,
                        namaBarang = state.nama.trim(),
                        jumlah = jumlahInt,
                        kategoriId = state.kategoriId,
                        updatedAt = nowString()
                    )
                    dao.insert(newBarang)
                }
                
                hideDialog()
            } catch (e: Exception) {
                _errorMessage.value = "Terjadi kesalahan: ${e.message}"
            }
        }
    }

    fun deleteBarang(barang: BarangManualEntity) {
        viewModelScope.launch {
            try {
                dao.delete(barang)
            } catch (e: Exception) {
                _errorMessage.value = "Gagal menghapus: ${e.message}"
            }
        }
    }
}
