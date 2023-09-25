package com.paulik.mapmodule.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.paulik.mapmodule.R
import com.paulik.mapmodule.databinding.ActivityRootBinding
import com.paulik.mapmodule.ui.map.MapFragment
import com.paulik.mapmodule.ui.markers.ListMarkersFragment

class RootActivity : AppCompatActivity(),
    ListMarkersFragment.Controller {

    private lateinit var binding: ActivityRootBinding
    private val listMarkersFragment = ListMarkersFragment.newInstance()
    private val mapFragment = MapFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        setCurrentFragment(mapFragment)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mapSwitch -> setCurrentFragment(mapFragment)
                R.id.listMarkers -> setCurrentFragment(listMarkersFragment)
            }
            true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setCurrentFragment(MapFragment())
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_frame_layout, fragment)
            addToBackStack("")
            commit()
        }
}