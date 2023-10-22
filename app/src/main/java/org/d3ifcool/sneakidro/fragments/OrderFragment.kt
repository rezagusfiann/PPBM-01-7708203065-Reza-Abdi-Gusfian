package org.d3ifcool.sneakidro.fragments

import android.content.Intent
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.d3ifcool.sneakidro.R
import org.d3ifcool.sneakidro.activities.DetailPemesananActivity
import org.d3ifcool.sneakidro.adapter.OrderAdapter
import org.d3ifcool.sneakidro.data.DATA_PEMESANAN
import org.d3ifcool.sneakidro.data.Pemesanan
import org.d3ifcool.sneakidro.databinding.FragmentOrderBinding

class OrderFragment : Fragment() {
    private var ref: DatabaseReference? = null
    private lateinit var binding: FragmentOrderBinding

    lateinit var list: ArrayList<Pemesanan>
    private lateinit var recyclerListView: RecyclerView
    lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(inflater, container, false)

        binding.recyclerView.setOnClickListener {
            activity?.let {
                val intent = Intent(it, DetailPemesananActivity::class.java)
                it.startActivity(intent)
            }
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

        // Attach a listener to read the data at our posts reference
        getOrder(view, userId)
    }

    private fun getOrder(view: View, userId: String) {
        recyclerListView = view.findViewById(R.id.recyclerView)
        ref = FirebaseDatabase.getInstance().getReference(DATA_PEMESANAN)
        recyclerListView.setHasFixedSize(true)
        recyclerListView.layoutManager = LinearLayoutManager(requireContext())

        list = ArrayList()
        orderAdapter = OrderAdapter(list)
        recyclerListView.adapter = orderAdapter

        ref!!.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    if(snapshot.exists()){
                        for (dataSnapshot: DataSnapshot in snapshot.children) {
                            val order = dataSnapshot.getValue(Pemesanan::class.java)
                            list.add(order!!)
                        }
                    }else{
                        binding.illustration.visibility = View.VISIBLE
                        binding.tidakAdaPesanan.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    }

                    orderAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

}