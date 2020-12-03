package com.example.burnjams

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.example.burnjams.databinding.ActivityMainBinding
import com.example.burnjams.fragments.HomeFragment
import com.example.burnjams.fragments.WorkoutsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            handleBottomNavigation(it.itemId)
        }
        binding.bottomNavigation.apply {
            selectedItemId = R.id.menu_home
            menu?.getItem(0)?.icon?.let {
                DrawableCompat.setTint(it, ContextCompat.getColor(this@MainActivity, R.color.white))
            }
            menu?.getItem(1)?.icon?.let {
                DrawableCompat.setTint(it, ContextCompat.getColor(this@MainActivity, R.color.white))
            }
        }
    }

    private fun handleBottomNavigation(menuItemId: Int): Boolean = when (menuItemId) {
        R.id.menu_home -> {
            swapFragments(HomeFragment())
            true
        }
        R.id.menu_workouts -> {
            swapFragments(WorkoutsFragment())
            true
        }
        else -> false
    }

    fun swapFragments(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_holder, fragment)
                .commit()
    }

    fun goToCreateActivity() {
        val intent = Intent(this, CreateWorkoutActivity::class.java)
        startActivity(intent)
    }
}