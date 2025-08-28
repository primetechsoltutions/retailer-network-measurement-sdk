package com.ptsl.network_sdk.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ptsl.network_sdk.data_model.entity.AuthEntity
import com.ptsl.network_sdk.data_model.entity.NetworkDataEntity
import com.ptsl.network_sdk.data_model.entity.WorkEntity
import com.ptsl.network_sdk.data_model.logger.EventLogModel


@Database(entities = [WorkEntity::class, NetworkDataEntity::class, AuthEntity::class, EventLogModel::class], version = 4, exportSchema = false)
abstract class NetworkDatabase: RoomDatabase(){
    abstract fun networkDao(): NetworkDao
}