package com.example.wanderer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.wanderer.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

const val PLACES_KEY = BuildConfig.GOOGLE_PLACES_API_KEY

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val nearbyFragment: Fragment = NearbyFragment()
        val searchFragment: Fragment = SearchFragment()
        val bookmarkFragment: Fragment = BookmarkFragment()
        val profileFragment: Fragment = ProfileFragment()

        val navbar: BottomNavigationView = findViewById(R.id.navbar)
        navbar.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.nav_nearby -> fragment = nearbyFragment
                R.id.nav_search -> fragment = searchFragment
                R.id.nav_bookmark -> fragment = bookmarkFragment
                R.id.nav_profile -> fragment = profileFragment
            }
            replaceFragment(fragment)
            true
        }
        navbar.selectedItemId = R.id.nav_nearby
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}