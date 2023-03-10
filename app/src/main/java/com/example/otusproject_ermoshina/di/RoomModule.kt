package com.example.otusproject_ermoshina.di

import android.content.Context
import androidx.room.Room
import com.example.otusproject_ermoshina.servise.room.MyDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        MyDataBase::class.java,
        "room_database"
    )
        .createFromAsset("prjct.db").build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun providePlayListDao(db: MyDataBase) = db.getPlayListDao()

    @Singleton
    @Provides
    fun provideQueryDao(db: MyDataBase) = db.getQueryDao()

    @Singleton
    @Provides
    fun provideVideoDao(db: MyDataBase) = db.getVideoDao()
}