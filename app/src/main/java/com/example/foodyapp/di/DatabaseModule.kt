package com.example.foodyapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodyapp.data.database.RecipesDao
import com.example.foodyapp.data.database.RecipesDatabase
import com.example.foodyapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): RoomDatabase {
        return Room.databaseBuilder(context, RecipesDatabase::class.java, Constants.DATABASE_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(database: RecipesDatabase): RecipesDao {
        return database.recipesDao()
    }

}