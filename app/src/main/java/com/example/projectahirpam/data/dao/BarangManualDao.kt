package com.example.projectahirpam.data.dao

import androidx.room.*
import com.example.projectahirpam.data.entity.BarangManualEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BarangManualDao {

    @Query("SELECT * FROM barang_manual WHERE userId = :userId ORDER BY namaBarang ASC")
    fun getAll(userId: Int): Flow<List<BarangManualEntity>>

    @Query("SELECT * FROM barang_manual WHERE userId = :userId ORDER BY namaBarang ASC")
    fun getAllSync(userId: Int): List<BarangManualEntity>

    @Query("SELECT * FROM barang_manual WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): BarangManualEntity?

    @Query("SELECT * FROM barang_manual WHERE userId = :userId AND namaBarang = :name LIMIT 1")
    suspend fun getByName(userId: Int, name: String): BarangManualEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(barang: BarangManualEntity): Long

    @Update
    suspend fun update(barang: BarangManualEntity)

    @Delete
    suspend fun delete(barang: BarangManualEntity)

    @Query("DELETE FROM barang_manual WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: Int)
}
