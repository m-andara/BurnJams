package com.example.burnjams

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.burnjams.databinding.ActivityCreateWorkoutBinding
import com.example.burnjams.fragments.CreateWorkoutFragment

class CreateWorkoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateWorkoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        swapFragments(CreateWorkoutFragment())
    }

    fun swapFragments(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_fragment_holder, fragment)
                .addToBackStack(null)
                .commit()
    }
}