package elektrogo.front

import android.app.Service
import android.content.Intent
import android.os.*
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import elektrogo.front.controller.ChatController
import elektrogo.front.controller.FrontendController
import elektrogo.front.controller.session.SessionController
import elektrogo.front.ui.chatConversation.ChatConversationViewModel


class ChatService : Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private var notificationId = 1
    private lateinit var builder: NotificationCompat.Builder
    private var context = this
    private lateinit var activeChats: ArrayList<String>
    private lateinit var prevConversation: ArrayList<elektrogo.front.model.Message>
    private lateinit var conversation: ArrayList<elektrogo.front.model.Message>
    private lateinit var currentUser: String
    private var firstIteration: Int = 0

    private suspend fun getActiveChats() {
        activeChats = FrontendController.getChatList(SessionController.getUsername(this))
    }

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
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
        currentUser = SessionController.getUsername(this)
        prevConversation = arrayListOf()
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

        //check if there are new messages coming from backend and send a "message" to display notification
        /*
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }
        */

        while (true) {
            /* METODO PRIMITIVO
            activeChats = ChatController.getChatList(currentUser)
            for (userB in activeChats) {
                conversation = ChatController.getConversation(currentUser, userB)
                if (firstIteration == 0) {
                    prevConversation = conversation
                    firstIteration = 1
                }
                if (conversation.size > prevConversation.size) {
                    var index: Int = prevConversation.size
                    val limit: Int = conversation.size
                    prevConversation = conversation
                    while (index + 1 < limit ) {
                        val message: elektrogo.front.model.Message = conversation[index+1]
                        //name of the user who sent the message
                        builder.setContentTitle(message.sender)
                        //content of the message
                        builder.setContentText(message.message)
                        //photo of the user who sent the message
                        //builder.setLargeIcon()

                        // For each start request, send a message to start a job and deliver the
                        // start ID so we know which request we're stopping when we finish the job
                        serviceHandler?.obtainMessage()?.also { msg ->
                            msg.arg1 = startId
                            serviceHandler?.sendMessage(msg)
                        }
                        ++index
                    }
                }
            }
             */

             // Implementar nuevo metodo con FrontendController.getReceivedMessages(currentUser)
             Thread.sleep(4000)
            // If we get killed, after returning from here, restart
            return START_STICKY
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
    }
}
