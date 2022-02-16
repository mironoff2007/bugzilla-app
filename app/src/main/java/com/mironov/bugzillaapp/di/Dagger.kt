package com.mironov.bugzillaapp.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.mironov.bugzillaapp.data.DataShared
import com.mironov.bugzillaapp.data.Repository
import com.mironov.bugzillaapp.data.room.BugsDatabase
import com.mironov.bugzillaapp.ui.BugListFragmentViewModel
import com.mironov.bugzillaapp.ui.BugsListFragment
import com.mironov.bugzillaapp.ui.MainActivity
import com.mironov.newsapp.di.*
import dagger.*
import dagger.multibindings.IntoMap
import javax.inject.Scope

@Scope
annotation class AppScope

@Component(modules = [AppModule::class, RetrofitModule::class, AppBindsModule::class,  RoomModule::class])
interface AppComponent  {
    fun inject(activity: MainActivity)
    fun inject(bugsListFragment: BugsListFragment) {

    }


    val factory: MultiViewModelFactory

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun context(context: Context): Builder
    }
}

@Module
class AppModule() {

    @Provides
    fun provideRepository(dataShared: DataShared, bugsDB: BugsDatabase): Repository {
        return Repository(dataShared,  bugsDB)
    }

    @Provides
    fun provideDataShared(context: Context): DataShared {
        return DataShared(context)
    }
}
@Module()
interface AppBindsModule {
    @Binds
    @[IntoMap ViewModelKey(BugListFragmentViewModel::class)]
    fun provideBugListFragmentViewModel(mainViewModel: BugListFragmentViewModel): ViewModel

}






