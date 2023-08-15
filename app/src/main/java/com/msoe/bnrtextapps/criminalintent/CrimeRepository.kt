package com.msoe.bnrtextapps.criminalintent

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.msoe.bnrtextapps.criminalintent.database.CrimeDatabase
import kotlinx.coroutines.flow.Flow
import java.util.UUID

private const val TAG = "CrimeRepository"

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context) {

    private val database: CrimeDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            CrimeDatabase::class.java,
            DATABASE_NAME
        )
        .createFromAsset(DATABASE_NAME)
        .allowMainThreadQueries()
        .build()

    fun getCrimes(): Flow<List<Crime>> {
        Log.d(TAG, "in Repo:getCrimes()")
        return database.crimeDao().getCrimes()
    }

    suspend fun getCrime(id: UUID): Crime = database.crimeDao().getCrime(id)

    suspend fun updateCrime(crime: Crime) {
        database.crimeDao().updateCrime(crime)
    }

    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}