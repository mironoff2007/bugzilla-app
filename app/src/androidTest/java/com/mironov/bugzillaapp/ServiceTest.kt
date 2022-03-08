package com.mironov.bugzillaapp

import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ServiceTestRule
import com.mironov.bugzillaapp.di.DaggerTestAppComponent
import com.mironov.bugzillaapp.di.TestAppComponent
import com.mironov.bugzillaapp.ui.CheckNewBugsService
import com.mironov.bugzillaapp.ui.CheckNewBugsService.Companion.EXTRAS_KEY
import com.mironov.bugzillaapp.ui.CheckNewBugsService.Companion.INITIAL_DELAY_KEY
import com.mironov.bugzillaapp.ui.CheckNewBugsService.Companion.TIMER_PERIOD_KEY
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit


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
        var newBugs=false
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val checkNewBugsService = Intent(appContext, CheckNewBugsService::class.java)
        val extras = Bundle()
        extras.putLong(TIMER_PERIOD_KEY,100)
        extras.putLong(INITIAL_DELAY_KEY,100)
        checkNewBugsService.putExtra(EXTRAS_KEY,extras)

        val binder: IBinder = serviceRule.bindService(checkNewBugsService)

        val service: CheckNewBugsService = (binder as CheckNewBugsService.LocalBinder).service

        service.repository=repository

        serviceRule.startService(checkNewBugsService)

        sleep(1000)

        newBugs=service.newBugs.get()

        service.stopService()

        assertEquals(newBugs,true)
    }
}
