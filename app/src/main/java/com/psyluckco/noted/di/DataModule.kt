package com.psyluckco.noted.di

import android.content.Context
import androidx.room.Room
import com.psyluckco.noted.data.local.TaskDao
import com.psyluckco.noted.data.local.TaskDatabase
import com.psyluckco.noted.data.network.NetworkDataSource
import com.psyluckco.noted.data.network.RetrofitNetworkDataSource
import com.psyluckco.noted.data.network.TaskApi
import com.psyluckco.noted.data.repository.CombinedTaskRepository
import com.psyluckco.noted.data.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTaskRepository(repository: CombinedTaskRepository): TaskRepository

}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(data: RetrofitNetworkDataSource): NetworkDataSource

}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApi(retrofit: Retrofit)  : TaskApi {
        return retrofit.create(TaskApi::class.java)
    }

}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): TaskDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            TaskDatabase::class.java,
            "task.db"
        ).build()
    }

    @Provides
    fun provideTaskDao(database: TaskDatabase): TaskDao = database.taskDao()
}