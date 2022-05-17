package elektrogo.front

import android.app.NotificationChannel
import android.app.Service
import android.content.Intent
import android.os.*
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class ChatService : Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private var notificationId = 1
    private lateinit var builder: NotificationCompat.Builder
    private var context = this

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                Thread.sleep(3000)
                with(NotificationManagerCompat.from(context)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(notificationId, builder.build())
                    notificationId += 1
                }

                Thread.sleep(7000)
                with(NotificationManagerCompat.from(context)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(notificationId, builder.build())
                    notificationId += 1
                }


            } catch (e: InterruptedException) {
                // Restore interrupt status.
                Thread.currentThread().interrupt()
            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            // Get the HandlerThread's Looper and use it for our Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        context = this

        val textTitle = "New Notification"
        val textContent = "text of the notification"
        builder = NotificationCompat.Builder(context, "ChatChannel")
            .setSmallIcon(R.drawable.ic_baseline_chat_24)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
    }
}
