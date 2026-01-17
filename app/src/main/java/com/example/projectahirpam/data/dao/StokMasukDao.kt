package com.example.projectahirpam.data.dao

import androidx.room.*
import com.example.projectahirpam.data.entity.StokMasukEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StokMasukDao {

    @Query("SELECT * FROM stok_masuk WHERE userId = :userId ORDER BY id DESC")
    fun getAll(userId: Int): Flow<List<StokMasukEntity>>

    @Insert
    suspend fun insert(data: StokMasukEntity)

    @Query("DELETE FROM stok_masuk WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: Int)
}
