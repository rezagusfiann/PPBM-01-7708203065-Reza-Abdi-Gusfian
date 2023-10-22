package org.d3ifcool.sneakidro.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3ifcool.sneakidro.databinding.ActivityMekanismePembayaranBinding

class MekanismePembayaranActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMekanismePembayaranBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMekanismePembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backMekanisme.setOnClickListener {
            finish()
        }
    }
}