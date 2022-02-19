package com.mironov.bugzillaapp.ui

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.mironov.bugzillaapp.appComponent
import com.mironov.bugzillaapp.data.Repository
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

    @Inject
    protected lateinit var repository: Repository

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)

    private val bugsCount = AtomicInteger(0)
    
    private var serviceId=0

    override fun onCreate() {
        super.onCreate()
        applicationContext.appComponent.inject(this)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        id++
        serviceId=id

        val notification=buildNotification(CONTENT_NO_BUGS)
        startForeground(serviceId, notification)

        serviceScope.launch(Dispatchers.Default) {
            fixedRateTimer(TIMER_NAME, false, 0L, TIMER_PERIOD)
            {
                repository.getBugsFromNetworkByDate(DateUtil.getTodayDate()).enqueue(object :
                    Callback<ApiResponse?> {
                    override fun onResponse(
                        call: Call<ApiResponse?>,
                        response: Response<ApiResponse?>
                    ) {
                        if (response.body() != null) {
                            if (response.body()!!.bugs!!.size > bugsCount.get()) {
                                updateNotification(notification)
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

    private fun buildNotification(contextText:String): Notification {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(BUGS_SERVICE_CHANNEL_ID, BUGS_SERVICE_CHANNEL_NAME)
            } else {
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(SERVICE_TITLE)
            .setContentText(contextText)
            .setSmallIcon(R.drawable.ic_popup_sync)
            .build()
    }

    private fun updateNotification(notification:Notification) {
        buildNotification(CONTENT_NEW_BUGS)
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

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        var id=0
        const val TIMER_PERIOD = 1000L
        const val SERVICE_TITLE = "Bugs service"
        const val CONTENT_NO_BUGS = "No new bags"
        const val CONTENT_NEW_BUGS = "New bags"
        const val BUGS_SERVICE_EXTRA = "BUGS_SERVICE_EXTRA"
        const val TIMER_NAME = "TIMER_NAME"
        const val BUGS_SERVICE_CHANNEL_ID = "BUGS_SERVICE_CHANNEL_ID"
        const val BUGS_SERVICE_CHANNEL_NAME = "BUGS_SERVICE_CHANNEL_NAME"
    }
}