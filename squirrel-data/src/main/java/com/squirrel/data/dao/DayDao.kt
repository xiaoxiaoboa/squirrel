package com.squirrel.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.squirrel.data.entity.DayEntity
import com.squirrel.data.entity.DayWithItem
import kotlinx.coroutines.flow.Flow

@Dao
interface DayDao {
//    @Insert
//    suspend fun insert(day: DayEntity): Long

    @Update
    suspend fun update(day: DayEntity)

    @Delete
    suspend fun delete(day: DayEntity)

    @Transaction
    @Query("SELECT * FROM days WHERE days.year = :year AND days.month = :month ORDER BY day DESC")
    fun getDayByMonth(year: Int, month: Int): Flow<List<DayWithItem>>

    @Transaction
    @Query("SELECT * FROM days WHERE days.year = :year AND days.month = :month AND days.day = :day")
    suspend fun getDayByDate(year: Int, month: Int, day: Int): DayEntity?


}