package elektrogo.front

import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.*
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import elektrogo.front.controller.ChatController
import elektrogo.front.controller.session.SessionController


class ChatService : Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private var notificationId = 1
    private lateinit var builder: NotificationCompat.Builder
    private var context = this
    private lateinit var prevMessages: ArrayList<elektrogo.front.model.Message>
    private lateinit var messages: ArrayList<elektrogo.front.model.Message>
    private lateinit var currentUser: String
    private var interrupted: Boolean = false

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                while (true and !interrupted) {
                    messages = ChatController.getReceivedMessages(currentUser)
                    if (messages.size > prevMessages.size) {
                        var index: Int = prevMessages.size
                        val limit: Int = messages.size
                        prevMessages = messages
                        while (index < limit ) {
                            val message: elektrogo.front.model.Message = messages[index]
                            //name of the user who sent the message
                            builder.setContentTitle(message.sender)
                            //content of the message
                            builder.setContentText(message.message)
                            /*
                            //photo of the user who sent the message
                            val defaultImage = R.id.userImage
                            var imagePath = message.sender?.let { ChatController.getUsersProfilePhoto(it) }

                            if (imagePath == "null") {
                                imagePath = defaultImage.toString()
                            }
                            val myBitmap = BitmapFactory.decodeFile(imagePath)
                            builder.setLargeIcon(myBitmap)
                             */
                            with(NotificationManagerCompat.from(context)) {
                                // notificationId is a unique int for each notification that you must define
                                notify(notificationId, builder.build())
                                notificationId += 1
                            }
                            ++index
                        }
                    }
                    Thread.sleep(4000)
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
        currentUser = SessionController.getUsername(this)
        prevMessages = ChatController.getReceivedMessages(currentUser)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        context = this

        interrupted = false

        val textTitle = "New Notification"
        val textContent = "text of the notification"
        builder = NotificationCompat.Builder(context, "ChatChannel")
            .setSmallIcon(R.drawable.ic_baseline_chat_24)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

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
        interrupted = true
        //Toast.makeText(context, "service done", Toast.LENGTH_SHORT).show()
    }
}
