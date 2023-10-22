package org.d3ifcool.sneakidro.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import org.d3ifcool.sneakidro.R
import org.d3ifcool.sneakidro.fragments.HomeFragment
import org.d3ifcool.sneakidro.fragments.OrderFragment
import org.d3ifcool.sneakidro.fragments.ProfileFragment

class SneakidroActivity : AppCompatActivity() {
    private var chipNavigationBar: ChipNavigationBar? = null
    private var profileFragment: ProfileFragment? = null
    private var homeFragment: HomeFragment? = null
    private var orderFragment: OrderFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sneakidro)

        chipNavigationBar = findViewById(R.id.bottom_nav_menu)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()

        chipNavigationBar?.setOnItemSelectedListener(object : ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                when (id) {
                    R.id.bottom_nav_order -> {
                        if (orderFragment == null) {
                            orderFragment = OrderFragment()
                        }
                        replaceFragment(orderFragment!!)
                    }
                    R.id.bottom_nav_home -> {
                        if (homeFragment == null) {
                            homeFragment = HomeFragment()
                        }
                        replaceFragment(homeFragment!!)
                    }
                    R.id.bottom_nav_profile -> {
                        if (profileFragment == null) {
                            profileFragment = ProfileFragment()
                        }
                        replaceFragment(profileFragment!!)
                    }
                }
            }
        })
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}