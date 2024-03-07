package com.udacity.asteroidradar.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseAsteroid constructor(
    @PrimaryKey
    var id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)