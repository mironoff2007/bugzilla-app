package com.mironov.bugzillaapp.di

import android.content.Context
import com.mironov.bugzillaapp.MockRepository
import com.mironov.bugzillaapp.ServiceTest
import com.mironov.bugzillaapp.data.BaseRepository
import dagger.*

@Component(modules = [AppModule::class])
interface TestAppComponent {

    fun injectTest(instrumentedTest: ServiceTest)

    @Component.Builder
    interface Builder {

        fun build(): TestAppComponent

        @BindsInstance
        fun context(context: Context): Builder
    }

    @Module
    class AppModule() {
        @Provides
        fun provideRepository(): BaseRepository {
            return MockRepository()
        }
    }
}





