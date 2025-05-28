package com.example.seriesfilm.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.seriesfilm.R
import com.example.seriesfilm.activities.Login

class SettingsFragment : Fragment() {
    private lateinit var logoutLayout: ConstraintLayout
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        initViews(view)
        setupClickListeners()
        return view
    }

    private fun initViews(view: View) {
        logoutLayout = view.findViewById(R.id.logoutLayout)
    }

    companion object {
        private const val TOKEN_KEY = "auth_token"
        private const val USERNAME_KEY = "username"
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
}
