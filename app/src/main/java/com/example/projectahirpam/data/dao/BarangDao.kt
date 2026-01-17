package com.example.projectahirpam.data.dao

import androidx.room.*
import com.example.projectahirpam.data.entity.BarangEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BarangDao {

    @Query("SELECT * FROM barang WHERE userId = :userId ORDER BY namaBarang ASC")
    fun getAll(userId: Int): Flow<List<BarangEntity>>

    @Query("SELECT * FROM barang WHERE userId = :userId ORDER BY namaBarang ASC")
    fun getAllSync(userId: Int): List<BarangEntity>

    @Query("SELECT * FROM barang WHERE kategoriId = :kategoriId ORDER BY namaBarang ASC")
    fun getAllByKategori(kategoriId: Int): Flow<List<BarangEntity>>

    @Query("SELECT * FROM barang WHERE kategoriId = :kategoriId AND namaBarang LIKE '%' || :query || '%' ORDER BY namaBarang ASC")
    fun searchInKategori(kategoriId: Int, query: String): Flow<List<BarangEntity>>

    @Query("SELECT * FROM barang WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): BarangEntity?

    @Query("SELECT * FROM barang WHERE userId = :userId AND namaBarang = :name LIMIT 1")
    suspend fun getByName(userId: Int, name: String): BarangEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(barang: BarangEntity): Long

    @Update
    suspend fun update(barang: BarangEntity)

    @Delete
    suspend fun delete(barang: BarangEntity)

    @Query("SELECT COUNT(*) FROM barang WHERE kategoriId = :kategoriId AND namaBarang = :nama")
    suspend fun countByNameInKategori(kategoriId: Int, nama: String): Int

    @Query("DELETE FROM barang WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: Int)
}
