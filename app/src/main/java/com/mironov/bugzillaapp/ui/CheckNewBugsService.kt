package com.mironov.bugzillaapp.ui

import android.app.*
import android.app.PendingIntent.getService
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.mironov.bugzillaapp.R
import com.mironov.bugzillaapp.appComponent
import com.mironov.bugzillaapp.data.BaseRepository
import com.mironov.bugzillaapp.data.retrofit.ApiResponse
import com.mironov.bugzillaapp.domain.DateUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer


class CheckNewBugsService : Service() {

    private lateinit var channelId: String

    @Inject
    lateinit var repository: BaseRepository

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)

    private val bugsCount = AtomicInteger(0)

    private var serviceId = 1

    // Binder given to clients
    private val mBinder: IBinder = LocalBinder()

    override fun onCreate() {
        super.onCreate()
        applicationContext.appComponent.inject(this)

        channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(BUGS_SERVICE_CHANNEL_ID, BUGS_SERVICE_CHANNEL_NAME)
            } else {
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }
        id++
        serviceId = id

    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        Log.d("My_tag", intent.action.toString())

        if (ACTION_STOP_SERVICE == intent.action) {
            stopService()
        }

        val notification =
            buildNotification(applicationContext.getString(com.mironov.bugzillaapp.R.string.no_new_bugs))
        startForeground(serviceId, notification)

        serviceScope.launch(Dispatchers.Default) {
            fixedRateTimer(TIMER_NAME, false, INITIAL_DELAY, TIMER_PERIOD)
            {
                repository.getBugsFromNetworkByDate(DateUtil.getTodayDate()).enqueue(object :
                    Callback<ApiResponse?> {
                    override fun onResponse(
                        call: Call<ApiResponse?>,
                        response: Response<ApiResponse?>
                    ) {
                        if (response.body() != null) {
                            if (response.body()!!.bugs!!.size > bugsCount.get()) {
                                updateNotification()
                                bugsCount.set(response.body()!!.bugs!!.size)
                                serviceScope.launch(Dispatchers.IO) {
                                    repository.saveBugsToDb(response.body()!!.bugs!!)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {}
                })
            }
        }
        return START_NOT_STICKY
    }

    private fun buildNotification(contextText: String): Notification {
        val stopSelf = Intent(ACTION_STOP_SERVICE)
        stopSelf.setClass(this@CheckNewBugsService, CheckNewBugsService::class.java)
        stopSelf.action = ACTION_STOP_SERVICE

        val pStopSelf = getService(this, 0, stopSelf, PendingIntent.FLAG_CANCEL_CURRENT)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(applicationContext.getString(R.string.bug_service_name))
            .setContentText(contextText)
            .setSmallIcon(R.drawable.ic_bug)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel,
                applicationContext.getString(R.string.close),
                pStopSelf
            )
            .build()
    }

    private fun updateNotification() {
        val notification =
            buildNotification(applicationContext.getString(R.string.new_bugs))
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(serviceId, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    fun stopService() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    inner class LocalBinder : Binder() {
        val service: CheckNewBugsService
            get() = this@CheckNewBugsService
    }

    companion object {
        var id = 0
        const val TIMER_PERIOD = 1000L
        const val INITIAL_DELAY = 1000L
        const val TIMER_NAME = "TIMER_NAME"
        const val ACTION_STOP_SERVICE = "STOP"
        const val BUGS_SERVICE_CHANNEL_ID = "BUGS_SERVICE_CHANNEL_ID"
        const val BUGS_SERVICE_CHANNEL_NAME = "BUGS_SERVICE_CHANNEL_NAME"
    }
}