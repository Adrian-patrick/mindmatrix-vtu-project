package com.mindmatrix.raithavarta.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TipDao {
    @Query("SELECT * FROM tips WHERE category = :category OR :category = 'All'")
    fun getTipsByCategory(category: String): Flow<List<TipEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTips(tips: List<TipEntity>)

    @Query("SELECT COUNT(*) FROM tips")
    suspend fun getCount(): Int
}
