package com.example.hydrosmart.afterlogin.ui.notifications

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.hydrosmart.databinding.FragmentNotificationsBinding
import com.example.hydrosmart.utils.TimePickerFragment

class NotificationsFragment : Fragment(),
    TimePickerFragment.DialogTimeListener {

    private var _binding: FragmentNotificationsBinding? = null
    private lateinit var alarmReceiver: AlarmReceiver

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(
                    requireContext(),
                    "Notifications permission granted",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Notifications permission rejected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        alarmReceiver = AlarmReceiver()
        setAlarm()
    }

    private fun setAlarm() {
        binding.apply {
            imgClock.setOnClickListener {
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(childFragmentManager, TIME_PICKER_REPEAT_TAG)
            }

            btReminder.setOnClickListener {
                val repeatTime = binding.tvClockTime.text.toString()
                val repeatMessage = binding.edtRepeatingMessage.text.toString()
                alarmReceiver.setRepeatingAlarm(
                    requireContext(),
                    repeatTime, repeatMessage
                )
            }

            btCancel.setOnClickListener {
                alarmReceiver.cancelAlarm(requireContext())
            }
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {

    }

    companion object {
        const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}