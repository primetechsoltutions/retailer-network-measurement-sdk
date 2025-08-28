package com.ptsl.network_sdk.utils


import android.content.Context
import androidx.room.Room
import com.ptsl.network_sdk.db.NetworkDao
import com.ptsl.network_sdk.db.NetworkDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton
import kotlin.coroutines.EmptyCoroutineContext


@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext applicationContext: Context): NetworkDatabase {
        return Room.databaseBuilder(
            applicationContext,
            NetworkDatabase::class.java,
            "network_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideNetworkDao(database: NetworkDatabase): NetworkDao {
        return database.networkDao()
    }


    @Singleton
    @Provides
    fun provideCoroutineScope() =
        CoroutineScope(EmptyCoroutineContext + Dispatchers.Default + SupervisorJob())

}
