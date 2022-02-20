package com.mironov.bugzillaapp

import android.content.Intent
import android.os.IBinder
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ServiceTestRule
import com.mironov.bugzillaapp.data.BaseRepository

import com.mironov.bugzillaapp.di.DaggerTestAppComponent
import com.mironov.bugzillaapp.di.TestAppComponent
import com.mironov.bugzillaapp.ui.CheckNewBugsService
import okhttp3.internal.wait
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
 class ServiceTest {

    private lateinit var appComponent: TestAppComponent

    protected var repository=MockRepository()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        appComponent = DaggerTestAppComponent.builder()
            .context(appContext)
            .build()
        appComponent.injectTest(this)

    }

    @get:Rule
    val serviceRule = ServiceTestRule.withTimeout(60L, TimeUnit.SECONDS);

    @Test
    fun testBugService() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // Запускаем свой ForegroundService
        val checkNewBugsService = Intent(appContext, CheckNewBugsService::class.java)

        // Bind the service and grab a reference to the binder.
        val binder: IBinder = serviceRule.bindService(checkNewBugsService)

        // Get the reference to the service, or you can call
        // public methods on the binder directly.
        val service: CheckNewBugsService = (binder as CheckNewBugsService.LocalBinder).service

        service.repository=repository

        serviceRule.startService(checkNewBugsService)

        sleep(100000)

        service.stopService()

        assertEquals("com.mironov.bugzillaapp", appContext.packageName)
    }
}
