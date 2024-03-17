package com.udacity.asteroidradar.db

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.models.Asteroid
import kotlinx.coroutines.flow.Flow


@Dao
interface AsteroidDao {

    @Query("SELECT * FROM databaseAsteroid WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC")
    fun getAsteroidsByCloseApproachDate(startDate: String, endDate: String): Flow<List<Asteroid>>

    @Query("SELECT * FROM databaseAsteroid ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): Flow<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("DELETE FROM databaseAsteroid WHERE closeApproachDate < :today")
    fun deletePreviousDayAsteroids(today: String): Int
}

@Database(entities = [DatabaseAsteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDB : RoomDatabase() {

    abstract val asteroidDao: AsteroidDao

    companion object {

        @Volatile
//        private lateinit var INSTANCE: AsteroidDB
        private var INSTANCE: AsteroidDB? = null
        fun provideDatabase(context: Context): AsteroidDB {

            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        }


    private fun buildDatabase(context: Context) =
        Room.databaseBuilder(context, AsteroidDB::class.java, "asteroids").build()
    }

}


