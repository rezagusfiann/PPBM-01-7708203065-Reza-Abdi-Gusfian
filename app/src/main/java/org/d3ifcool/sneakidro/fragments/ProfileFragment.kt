package org.d3ifcool.sneakidro.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.sucho.placepicker.AddressData
import com.sucho.placepicker.Constants
import com.sucho.placepicker.MapType
import com.sucho.placepicker.PlacePicker
import org.d3ifcool.sneakidro.R
import org.d3ifcool.sneakidro.activities.LoginActivity
import org.d3ifcool.sneakidro.databinding.FragmentProfileBinding
import java.util.*


class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var storage : FirebaseStorage
    private lateinit var firebaseUser: FirebaseUser
    //binding fragment profile
    private lateinit var binding: FragmentProfileBinding
    //imageuri
    private var imageUri:Uri? = null
    //progress dialog
    private lateinit var progressDialog: ProgressDialog
    //location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    // open gallery
    private var galleryActivityResultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        //progress dialog
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Mohon Tunggu")
        progressDialog.setCanceledOnTouchOutside(false)

        //firebase
        auth = FirebaseAuth.getInstance()
        loadUserInfo()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        binding.editButton.setOnClickListener {
            uploadData()
        }
        binding.logoutButton.setOnClickListener {
            logout()
        }

        binding.lokasiButton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
                return@setOnClickListener
            }
            val location = fusedLocationClient.lastLocation
            location.addOnSuccessListener {
                if (it != null) {
                    val intent = PlacePicker.IntentBuilder()
                        .setLatLong(it.latitude, it.longitude)
                        .setAddressRequired(true)
                        .showLatLong(false)
                        .setMapType(MapType.NORMAL)
                        .build(requireActivity())
                    startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST)
                }
                else {
                    val intent = PlacePicker.IntentBuilder()
                        .setLatLong(-6.973049592891938, 107.63150300710856)
                        .setAddressRequired(true)
                        .showLatLong(false)
                        .setMapType(MapType.NORMAL)
                        .build(requireActivity())
                    startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST)
                }
            }
        }

        galleryActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                imageUri = data!!.data
                binding.imageViewProfil.setImageURI(imageUri)
            } else {
                Toast.makeText(requireContext(), "Batal", Toast.LENGTH_SHORT).show()
            }
        }

        //location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        getLocation()
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
                    val addressData = data?.getParcelableExtra<AddressData>(Constants.ADDRESS_INTENT)
                    binding.alamatText.text = addressData.toString().toEditable()
                } catch (e: Exception) {
                    e.message?.let { Log.e("MapsFragment", it) }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onPause() {
        super.onPause()
        getLocation()
    }

    override fun onResume() {
        super.onResume()
        getLocation()
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
                return
        }

        val location = fusedLocationClient.lastLocation
        location.addOnSuccessListener {
            if (it!=null) {
                val location = geocoder.getFromLocation(it.latitude,it.longitude, 1)
//                val address = "${location?.get(0)?.getAddressLine(0)}"
                val lokasi = "${location?.get(0)?.subAdminArea}, " + "${location?.get(0)?.countryName}"
//                fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
                binding.lokasi.text = lokasi
//                binding.alamatText.text = address.toEditable()
            }
        }
    }

    private var name = ""
    private var address = ""
    private var number = ""
    private fun uploadData() {
        // get data
        name = binding.namaText.text.toString().trim()
        address = binding.alamatText.text.toString().trim()
        number = binding.teleponText.text.toString().trim()

        //validate data
        if (name.isEmpty()) {
            //name not entered
            Toast.makeText(requireContext(),"Masukan Nama", Toast.LENGTH_SHORT).show()
        }
        else if (address.isEmpty()) {
            Toast.makeText(requireContext(),"Masukan Alamat", Toast.LENGTH_SHORT).show()
        }
        else if (number.isEmpty()) {
            Toast.makeText(requireContext(),"Masukan Nomor", Toast.LENGTH_SHORT).show()
        }
        else {
            //name is entered
            if (imageUri == null) {
                //update without image
                updateProfile("")
            }
            else {
                //update with image
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        progressDialog.setMessage("Mengunggah foto profil")
        progressDialog.show()

        //image path and name, use uid to replace previous
        val filePathAndName = "imageUrl/"+auth.uid

        //storage reference
        val reference = FirebaseStorage.getInstance().getReference(filePathAndName)
        reference.putFile(imageUri!!)
            .addOnSuccessListener {taskSnapshot->
                //image upload
                val uriTask:Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful) {
                    // do nothing, wait until resolves as false
                }
                val uploadedImageUrl = "${uriTask.result}"

                updateProfile(uploadedImageUrl)

                imageUri = null

            }
            .addOnFailureListener{e->
                //failed upload image
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"Gagal mengunggah foto profil karena ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateProfile(uploadedImageUrl: String) {
        progressDialog.setMessage("Simpan Profil")
        val ref = database.reference.child("username/$name")

        //setup info update to firebase
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["name"] = "username/$name"
        hashMap["name"] = name
        hashMap["address"] = address
        hashMap["number"] = number
        if (imageUri != null) {
            hashMap["imageUrl"] = uploadedImageUrl
        }

        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val reference = FirebaseDatabase.getInstance().getReference("Users")
                reference.child(auth.uid!!)
                    .updateChildren(hashMap)
                    .addOnSuccessListener {
                        //profile update
                        ref.setValue(name)
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(),"Profil tersimpan", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{e->
                        //failed upload image
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(),"Gagal untuk menyimpan profil karena ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun loadUserInfo() {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(auth.uid!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val email = "${snapshot.child("email").value}"
                    val imageUrl = "${snapshot.child("imageUrl").value}"
                    val name = "${snapshot.child("name").value}"
                    val number = "${snapshot.child("number").value}"
                    val address = "${snapshot.child("address").value}"

                    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

                    binding.floatingCamera.setOnClickListener {
                        pickImageGallery()
                    }
                    binding.namaText.text = name
                    binding.emailText.text = email
                    binding.teleponText.text = number.toEditable()
                    binding.alamatText.text = address.toEditable()

                    try {
                        Glide.with(this@ProfileFragment)
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_baseline_person)
                            .centerCrop()
                            .into(binding.imageViewProfil)
                    }
                    catch (_: Exception){
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        galleryActivityResultLauncher?.launch(intent)
    }

    private fun logout() {
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        val i = Intent(context, LoginActivity::class.java)
        activity?.finish()
        startActivity(i)

    }
}


