package com.example.seriesfilm.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.seriesfilm.R
import com.example.seriesfilm.fragments.FavoritesFragment
import com.example.seriesfilm.fragments.HomeFragment
import com.example.seriesfilm.fragments.SearchFragment
import com.example.seriesfilm.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, HomeFragment())
                .commit()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigatiomView)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.home -> selectedFragment = HomeFragment()
                R.id.search -> selectedFragment = SearchFragment()
                R.id.favorites -> selectedFragment = FavoritesFragment()
                R.id.settings -> selectedFragment = SettingsFragment()
            }

            // Замена фрагмента
            selectedFragment?.let {
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout, it).commit()
            }
            true
        }
    }
}