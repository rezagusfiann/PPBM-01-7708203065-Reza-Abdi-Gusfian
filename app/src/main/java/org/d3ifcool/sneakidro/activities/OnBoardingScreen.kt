package org.d3ifcool.sneakidro.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import org.d3ifcool.sneakidro.R
import org.d3ifcool.sneakidro.adapter.OnBoardingViewPagerAdapter
import org.d3ifcool.sneakidro.model.OnBoardingData

class OnBoardingScreen : AppCompatActivity() {
    private var onBoardingViewPagerAdapter: OnBoardingViewPagerAdapter? = null
    private var tabLayout: TabLayout? = null
    private var onBoardingViewPager : ViewPager? = null
    private var sharedPreferences: SharedPreferences? = null
    var skip: TextView? = null
    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(restorePrefData()){
            val i = Intent(applicationContext, LoginActivity::class.java)
            startActivity(i)
            finish()
        }

        setContentView(R.layout.activity_main)
        tabLayout = findViewById(R.id.tab_indicator)
        skip = findViewById(R.id.skip)

        val onBoardingData:MutableList<OnBoardingData> = ArrayList()
        onBoardingData.add(OnBoardingData(getString(R.string.judul1),getString(R.string.des1),
            R.drawable.phone
        ))
        onBoardingData.add(OnBoardingData(getString(R.string.judul2),getString(R.string.des2),
            R.drawable.courier_min
        ))
        onBoardingData.add(OnBoardingData(getString(R.string.judul3),getString(R.string.des3),
            R.drawable.shop_assets
        ))

        setOnBoardingViewPagerAdapter(onBoardingData)

        position = onBoardingViewPager!!.currentItem

        skip?.setOnClickListener {
            if(position < onBoardingData.size){
                savePrefData()
                val i = Intent(applicationContext, LoginActivity::class.java)
                finish()
                startActivity(i)
            }

            if (position == onBoardingData.size){
                savePrefData()
                val i = Intent(applicationContext, LoginActivity::class.java)
                finish()
                startActivity(i)
            }
        }

        tabLayout!!.addOnTabSelectedListener(object : OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                position = tab!!.position
                if (tab.position == onBoardingData.size - 1){
                    skip!!.text = getString(R.string.cekidot)
                }else{
                    skip!!.text = getString(R.string.skip)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun setOnBoardingViewPagerAdapter(onBoardingData: List<OnBoardingData>){
        onBoardingViewPager = findViewById(R.id.screenPager)
        onBoardingViewPagerAdapter = OnBoardingViewPagerAdapter(this, onBoardingData)
        onBoardingViewPager!!.adapter = onBoardingViewPagerAdapter
        tabLayout?.setupWithViewPager(onBoardingViewPager)
    }

    private fun savePrefData(){
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences!!.edit()
        editor.putBoolean("isFirstTimeRun", true)
        editor.apply()
    }

    private fun restorePrefData(): Boolean{
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences!!.getBoolean("isFirstTimeRun", false)

    }

}