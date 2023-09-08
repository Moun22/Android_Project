package fr.uparis.projetmobile

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.NavHostFragment.findNavController
import fr.uparis.projetmobile.fragments.list.ListFragment

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val it= context?.let { NavDeepLinkBuilder(it) }
            ?.setGraph(R.navigation.bd_nav)
            ?.setDestination(R.id.listFragmentToday)
            ?.createPendingIntent()
        intent!!.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val builder = NotificationCompat.Builder(context!!,"notifArrosage")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Arrosage de plantes")
            .setContentText("Vous aves des plantes à arroser")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(it)
        val notificationmanager= NotificationManagerCompat.from(context)
        notificationmanager.notify(1,builder.build())

    }
}