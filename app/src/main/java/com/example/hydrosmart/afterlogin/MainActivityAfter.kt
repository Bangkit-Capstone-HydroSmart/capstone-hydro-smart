package com.example.hydrosmart.afterlogin

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hydrosmart.R
import com.example.hydrosmart.afterlogin.ui.notifications.NotificationsFragment.Companion.TIME_PICKER_REPEAT_TAG
import com.example.hydrosmart.databinding.ActivityMainAfterBinding
import com.example.hydrosmart.utils.TimePickerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivityAfter : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var binding: ActivityMainAfterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainAfterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_profile
            )
        )
        if (supportActionBar != null) {
            setupActionBarWithNavController(navController, appBarConfiguration)
        }

        navView.setupWithNavController(navController)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        findViewById<Button>(R.id.bt_reminder).isEnabled = true

        when (tag) {
            TIME_PICKER_REPEAT_TAG -> findViewById<TextView>(R.id.tv_clock_time).text =
                dateFormat.format(calendar.time)
            else -> {
            }
        }
    }
}