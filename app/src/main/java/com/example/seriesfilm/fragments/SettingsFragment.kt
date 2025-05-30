package com.example.seriesfilm.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.seriesfilm.R
import com.example.seriesfilm.activities.Login

class SettingsFragment : Fragment() {
    private lateinit var logoutLayout: ConstraintLayout
    private lateinit var themeSwitchLayout: ConstraintLayout
    private lateinit var themeSwitch: Switch
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userNameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        initViews(view)
        loadUserData()
        setupThemeSwitch()
        setupClickListeners()
        return view
    }

    private fun initViews(view: View) {
        logoutLayout = view.findViewById(R.id.logoutLayout)
        userNameTextView = view.findViewById(R.id.userName)
        themeSwitchLayout = view.findViewById(R.id.themeSwitchLayout)
        themeSwitch = view.findViewById(R.id.themeSwitch)
        sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", MODE_PRIVATE)
    }

    private fun loadUserData() {
        val username = sharedPreferences.getString(USERNAME_KEY, "User") ?: "User"

        userNameTextView.text = username
    }

    private fun setupClickListeners() {
        sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", MODE_PRIVATE)

        logoutLayout.setOnClickListener {
            sharedPreferences.edit()
                .remove(TOKEN_KEY)
                .remove(USERNAME_KEY)
                .apply()

            val intent = Intent(requireContext(), Login::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun setupThemeSwitch() {
        val isDarkTheme = sharedPreferences.getBoolean(DARK_THEME_KEY, false)
        themeSwitch.isChecked = isDarkTheme

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean(DARK_THEME_KEY, isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            requireActivity().recreate()
        }
    }


    companion object {
        private const val TOKEN_KEY = "auth_token"
        private const val USERNAME_KEY = "username"
        private const val DARK_THEME_KEY = "dark_theme"
    }
}
