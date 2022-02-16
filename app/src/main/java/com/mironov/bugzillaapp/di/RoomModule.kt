package com.mironov.bugzillaapp.di

import android.content.Context
import com.mironov.bugzillaapp.data.room.BugsDatabase
import com.mironov.bugzillaapp.di.AppScope
import dagger.Module
import dagger.Provides

@Module
object RoomModule {

    @Provides
    fun provideArticleDatabase(context:Context): BugsDatabase {
        return BugsDatabase.getDatabase(context)
    }
}