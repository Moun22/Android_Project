package fr.uparis.projetmobile

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import fr.uparis.projetmobile.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var calendar: Calendar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBarWithNavController(findNavController(R.id.fragment))
        cNotification()
        calendar=Calendar.getInstance()
        calendar.time= Date()
        calendar.set(Calendar.HOUR_OF_DAY,8)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.SECOND,0)
        calendar.time
        alarmManager=getSystemService(ALARM_SERVICE) as AlarmManager
        val intent= Intent(this,AlarmReceiver::class.java)
        pendingIntent= PendingIntent.getBroadcast(this,0,intent,0)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent
        )

    }

    private fun cNotification() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val nom: CharSequence="notifArrossagReminderChannel"
            val desc="Channel"
            val imp = NotificationManager.IMPORTANCE_HIGH
            val channel= NotificationChannel("notifArrosage",nom,imp)
            channel.description=desc
            val notifManager= getSystemService(NotificationManager::class.java)
            notifManager.createNotificationChannel(channel )
        }

    }
}