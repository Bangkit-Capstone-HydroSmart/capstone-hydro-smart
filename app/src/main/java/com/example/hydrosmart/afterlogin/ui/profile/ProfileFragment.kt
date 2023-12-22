package com.example.hydrosmart.afterlogin.ui.profile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.bumptech.glide.Glide
import com.example.hydrosmart.R
import com.example.hydrosmart.ViewModelFactory
import com.example.hydrosmart.beforelogin.MainActivity
import com.example.hydrosmart.data.pref.UserPreference
import com.example.hydrosmart.databinding.FragmentProfileBinding
import com.example.hydrosmart.utils.DarkMode
import com.example.hydrosmart.utils.getImageUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding
    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory(UserPreference.getInstance(requireActivity().dataStore), requireActivity())
    }
    private var currentImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            childFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingFragment())
                .commit()
        }

        action()
        requestStoragePermission()
    }

    private fun requestStoragePermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE
            )
            setData()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.permission_already_granted), Toast.LENGTH_SHORT
            ).show()
            setData()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        @Suppress("DEPRECATION")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setData()
            } else {
                // Permission denied, show an error message to the user
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_denied), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setData() {
        val uid: String? = FirebaseAuth.getInstance().currentUser?.uid

        profileViewModel.getUser().observe(viewLifecycleOwner) {
            binding?.apply {
                uid?.let { uid ->
                    FirebaseDatabase.getInstance().reference.child("users").child(uid).get()
                        .addOnCompleteListener {
                            if (isAdded) {
                                tvName.text = it.result.child("name").value.toString()
                            }
                        }
                }
                tvEmail.text = it.email.toString()
                binding?.imgProfile?.let { it1 ->
                    Glide.with(requireContext()).load(it.image?.toUri())
                        .error(R.drawable.profile_user_default).into(it1)
                }
            }
        }

        binding?.imgAddPhoto?.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle(getString(R.string.dialog_title))
                setMessage(getString(R.string.dialog_message))
                setPositiveButton(getString(R.string.camera)) { _, _ ->
                    startCamera()
                }
                setNegativeButton(getString(R.string.galery)) { _, _ ->
                    startIntentGallery()
                }
            }.show()
        }
    }

    private fun startIntentGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Toast.makeText(requireContext(), getString(R.string.empty_media), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d(TAG, "showImage: $it")
            binding?.imgProfile?.setImageURI(it)
            saveImageUriToDataStore(it.toString())
        }
    }

    private fun saveImageUriToDataStore(image: String) {
        profileViewModel.getUser().observe(requireActivity()) {
            val user = it.copy(image = image)
            profileViewModel.saveUser(user)
        }
    }

    private fun action() {
        binding?.btLogout?.setOnClickListener {
            AlertDialog.Builder(requireActivity()).apply {
                setTitle(getString(R.string.dialog_title_logout))
                setMessage(getString(R.string.dialog_message_logout))
                setPositiveButton(getString(R.string.positive_bt_text)) { _, _ ->
                    profileViewModel.logout()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
                setNegativeButton(getString(R.string.negative_bt_text)) { _, _ ->
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.batal_logout_text), Toast.LENGTH_LONG
                    ).show()
                }
            }.show()
        }
    }

    class SettingFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val listPreference =
                findPreference<ListPreference>(getString(R.string.pref_key_dark))
            listPreference?.setOnPreferenceChangeListener { _, newValue ->
                val nightMode = DarkMode.valueOf(newValue.toString().uppercase(Locale.US))
                updateTheme(nightMode.value)
            }
        }

        private fun updateTheme(mode: Int): Boolean {
            AppCompatDelegate.setDefaultNightMode(mode)
            requireActivity().recreate()
            return true
        }

    }

    companion object {
        const val TAG = "IMAGE URI"
        const val STORAGE_PERMISSION_CODE = 101
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
