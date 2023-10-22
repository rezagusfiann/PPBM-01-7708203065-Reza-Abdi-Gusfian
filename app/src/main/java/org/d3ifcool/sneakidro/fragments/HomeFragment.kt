package org.d3ifcool.sneakidro.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import org.d3ifcool.sneakidro.R
import org.d3ifcool.sneakidro.activities.DeepCleanActivity
import org.d3ifcool.sneakidro.activities.SoftCleanActivity
import org.d3ifcool.sneakidro.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var firebaseUser: FirebaseUser

    //profile fragment bind
    private var profileFragment: ProfileFragment? = null

    //home fragment bind
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.softCard.setOnClickListener {
            activity?.let {
                val intent = Intent(it, SoftCleanActivity::class.java)
                it.startActivity(intent)
            }
        }

        binding.deepCard.setOnClickListener {
            activity?.let {
                val intent = Intent(it, DeepCleanActivity::class.java)
                it.startActivity(intent)
            }
        }

        binding.profileHome.setOnClickListener {
            if (profileFragment == null) {
                profileFragment = ProfileFragment()
            }
            replaceFragment(profileFragment!!)
        }

        auth = FirebaseAuth.getInstance()
        loadUserInfo()

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun loadUserInfo() {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(auth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val imageUrl = "${snapshot.child("imageUrl").value}"

                    try {
                        Glide.with(this@HomeFragment)
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .centerCrop()
                            .into(binding.profileHome)
                    } catch (_: Exception) {
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }



}
