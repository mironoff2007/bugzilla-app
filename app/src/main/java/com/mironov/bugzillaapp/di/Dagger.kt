package com.mironov.bugzillaapp.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.mironov.bugzillaapp.data.BaseRepository
import com.mironov.bugzillaapp.data.DataShared
import com.mironov.bugzillaapp.data.Repository
import com.mironov.bugzillaapp.data.room.BugsDatabase
import com.mironov.bugzillaapp.ui.*
import com.mironov.newsapp.di.*
import dagger.*
import dagger.multibindings.IntoMap
import javax.inject.Scope

@Scope
annotation class AppScope

@Component(modules = [AppModule::class, RetrofitModule::class, AppBindsModule::class,  RoomModule::class])
interface AppComponent  {
    fun inject(activity: MainActivity)
    fun inject(bugsListFragment: BugsListFragment)
    fun inject(prefsFragment: PrefsFragment)
    fun inject(checkNewBugsService: CheckNewBugsService)

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
    fun provideRepository(dataShared: DataShared, bugsDB: BugsDatabase): BaseRepository {
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






