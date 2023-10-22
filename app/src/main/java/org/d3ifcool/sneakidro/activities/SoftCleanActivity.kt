package org.d3ifcool.sneakidro.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import org.d3ifcool.sneakidro.R
import org.d3ifcool.sneakidro.databinding.SoftCleaningLayoutBinding
import org.d3ifcool.sneakidro.fragments.HomeFragment
import org.d3ifcool.sneakidro.fragments.OrderFragment
import org.d3ifcool.sneakidro.fragments.ProfileFragment

class SoftCleanActivity : AppCompatActivity() {
    private lateinit var binding: SoftCleaningLayoutBinding
    private var chipNavigationBar: ChipNavigationBar? = null
    private var profileFragment: ProfileFragment? = null
    private var homeFragment: HomeFragment? = null
    private var orderFragment: OrderFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SoftCleaningLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButtonSoft.setOnClickListener {
            finish()
        }
        chipNavigationBar = findViewById(R.id.bottom_nav_menu)

        chipNavigationBar?.setOnItemSelectedListener(object : ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                when (id) {
                    R.id.bottom_nav_order -> {
                        if (orderFragment == null) {
                            orderFragment = OrderFragment()
                        }
                        replaceFragment(orderFragment!!)
                        val card : CardView = findViewById(R.id.card_soft)
                        val card1 : CardView = findViewById(R.id.descSoft)
                        val card2 : CardView = findViewById(R.id.descPrice_soft)
                        val im : ImageButton = findViewById(R.id.backButton_soft)
                        val buttonOpen : Button = findViewById(R.id.pesanButton_soft)

                        buttonOpen.visibility = View.GONE
                        card.visibility = View.GONE
                        card1.visibility = View.GONE
                        card2.visibility = View.GONE
                        im.visibility = View.GONE
                    }
                    R.id.bottom_nav_home -> {
                        if (homeFragment == null) {
                            homeFragment = HomeFragment()
                        }
                        finish()
                    }
                    R.id.bottom_nav_profile -> {
                        if (profileFragment == null) {
                            profileFragment = ProfileFragment()
                        }
                        replaceFragment(profileFragment!!)
                        val card : CardView = findViewById(R.id.card_soft)
                        val card1 : CardView = findViewById(R.id.descSoft)
                        val card2 : CardView = findViewById(R.id.descPrice_soft)
                        val im : ImageButton = findViewById(R.id.backButton_soft)
                        val buttonOpen : Button = findViewById(R.id.pesanButton_soft)

                        buttonOpen.visibility = View.GONE
                        card.visibility = View.GONE
                        card1.visibility = View.GONE
                        card2.visibility = View.GONE
                        im.visibility = View.GONE
                    }
                }
            }
        })

        val buttonOpen : Button = findViewById(R.id.pesanButton_soft)
        buttonOpen.setOnClickListener {
            val myIntent = Intent(this, PesanSCActivity::class.java)
            startActivity(myIntent)
            finish()
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.soft_cleaning_layout, fragment)
            .commit()
    }
}


