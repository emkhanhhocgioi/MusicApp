package com.example.musicai.main.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.musicai.Components.HomeFragment
import com.example.musicai.R
import com.google.android.material.tabs.TabLayout

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hom)
        val tab = findViewById<TabLayout>(R.id.navigate_bar)

        setTabIcons(tab)
        val homeFrag = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.tab_frame, homeFrag)
            .commit()






    }

    fun setTabIcons(tab: TabLayout) {
        tab.getTabAt(0)?.setIcon(R.drawable.home)
        tab.getTabAt(1)?.setIcon(R.drawable.favorite_24)
        tab.getTabAt(2)?.setIcon(R.drawable.play_circle_24)
        tab.getTabAt(3)?.setIcon(R.drawable.download_24)
        tab.getTabAt(4)?.setIcon(R.drawable.person_24)



        val homeFrag = HomeFragment()
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                when (tab.position) {
                    0 -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.tab_frame, homeFrag)
                            .commit()
                    }
                    1 -> {

                    }
                    2 -> {
                        // Handle Playlists tab selection
                    }
                    3 -> {
                        // Handle Downloads tab selection
                    }
                    4 -> {
                        // Handle Profile tab selection
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                    when(tab.position){
                        0 -> {
                            homeFrag.onDestroy()
                        }
                        1 -> {
                            // Handle Favorites tab unselection
                        }
                        2 -> {
                            // Handle Playlists tab unselection
                        }
                        3 -> {
                            // Handle Downloads tab unselection
                        }
                        4 -> {
                            // Handle Profile tab unselection
                        }
                    }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Handle tab reselection
            }
        })
    }
    fun getTestApi (){


    }


}