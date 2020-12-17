package com.example.burnjams

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.burnjams.databinding.ActivityMainBinding
import com.example.burnjams.fragments.WorkoutsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun swapFragments(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_holder, fragment)
                .addToBackStack(null)
                .commit()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        swapFragments(WorkoutsFragment())
    }
}