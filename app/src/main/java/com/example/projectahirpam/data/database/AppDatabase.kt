package com.example.projectahirpam.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projectahirpam.data.dao.UserDao
import com.example.projectahirpam.data.dao.KategoriDao
import com.example.projectahirpam.data.dao.BarangDao
import com.example.projectahirpam.data.dao.BarangManualDao
import com.example.projectahirpam.data.dao.StokMasukDao
import com.example.projectahirpam.data.dao.StokKeluarDao
import com.example.projectahirpam.data.entity.UserEntity
import com.example.projectahirpam.data.entity.KategoriEntity
import com.example.projectahirpam.data.entity.BarangEntity
import com.example.projectahirpam.data.entity.BarangManualEntity
import com.example.projectahirpam.data.entity.StokMasukEntity
import com.example.projectahirpam.data.entity.StokKeluarEntity

@Database(entities = [UserEntity::class, KategoriEntity::class, BarangEntity::class, BarangManualEntity::class, StokMasukEntity::class, StokKeluarEntity::class], version = 5)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun kategoriDao(): KategoriDao
    abstract fun barangDao(): BarangDao
    abstract fun barangManualDao(): BarangManualDao
    abstract fun stokMasukDao(): StokMasukDao
    abstract fun stokKeluarDao(): StokKeluarDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "minicam_db"
                )
                    .fallbackToDestructiveMigration()
                    .build().also {
                        INSTANCE = it
                    }
            }
        }
    }
}
